/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.karrat.entity.Player
import org.karrat.internal.exitProcessWithMessage
import org.karrat.network.*
import org.karrat.network.translation.generateKeyPair
import org.karrat.play.Material
import org.karrat.server.FormattedPrintStream
import org.karrat.server.ReflectionPrintStream
import org.karrat.server.startConsoleInput
import org.karrat.world.Biome
import org.karrat.world.Dimension
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

public object Server {
    
    public var worlds: MutableList<World> = mutableListOf()
    public val players: Set<Player>
        get() {
            val result: MutableSet<Player> = mutableSetOf()
            worlds.forEach {
                result.addAll(it.players)
            }
            return result
        }
    
    public var sessions: MutableList<Session> = mutableListOf()
    public lateinit var socket: ServerSocketChannel
    public val auth: AuthServer = AuthServer()

    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    internal var tickTimeMillis: Long = 0L

    public fun start(port: Int) {
        System.setOut(
            if (Config.basicLogging) { FormattedPrintStream(System.out) }
            else { ReflectionPrintStream(System.out) }
        )
        println("Server starting.")
        loadResources()
        socket = ServerSocketChannel.open()
        runCatching {
            socket.bind(InetSocketAddress(InetAddress.getLocalHost(), port))
        }.onFailure {
            exitProcessWithMessage("Port $port is already in use! Shutting down server...", 1)
        }
        socket.configureBlocking(true)
        println("Bound to ip ${socket.localAddress} on port $port.")
        thread(name = "Console") {
            startConsoleInput()
        }
        thread(name = "socket") {
            while (true) {
                val session = Session(SocketChannel(socket.accept()))
                sessions.add(session)
                println("Accepted $session.")
            }
        }
        runBlocking {
            while (isActive) {
                tickTimeMillis =
                    measureTimeMillis {
                        tick() // Run game tick
                    }
                delay(maxOf(0L, (1000 / Config.tps) - tickTimeMillis))
            }
        }
    }

    public fun stop() {
        println("Terminating sessions.")
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }

    public fun tick(): Unit = runBlocking {
        sessions.removeIf { !it.isAlive }
        sessions.forEach { session ->
            launch {
                runCatching { session.handle() }
                    .onFailure {
                        println("$session disconnected; internal error: ${it.message}")
                        runCatching { session.disconnect("Internal error: ${it.message}") }
                        sessions.remove(session)
                    }
            }
        }
    }

    private fun loadResources() {
        measureTimeMillis {
            Biome.registerBiomes()
        }.let { println("Loaded ${Biome.biomes.size} biomes in ${it}ms.") }
        measureTimeMillis {
            Material.registerMaterials()
        }.let { println("Loaded ${Material.materials.size} materials in ${it}ms.") }
        measureTimeMillis {
            Dimension.registerDimensions()
        }.let { println("Loaded ${Dimension.dimensions.size} dimensions in ${it}ms.") }
    }

}