/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import kotlinx.coroutines.*
import org.karrat.command.Command
import org.karrat.configuration.eulaPrompt
import org.karrat.configuration.genServerFiles
import org.karrat.configuration.isFirstRun
import org.karrat.configuration.launchWithThreadCount
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
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

public object Server {
    
    public var worlds: MutableList<World> = mutableListOf()
    public var commands: MutableList<Command> = mutableListOf()
    public val players: MutableList<Player>
        get() {
            val result: MutableList<Player> = mutableListOf()
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
    
    @Suppress("BlockingMethodInNonBlockingContext")
    public fun start() {
        System.setOut(
            if (Config.basicLogging) { FormattedPrintStream(System.out) }
            else { ReflectionPrintStream(System.out) }
        )
        println("Server starting.")
        if (isFirstRun) {
            genServerFiles()
        }
        eulaPrompt()
        loadResources()
        socket = ServerSocketChannel.open()
        runCatching {
            socket.bind(InetSocketAddress(InetAddress.getLocalHost(), Config.port))
        }.onFailure {
            exitProcessWithMessage("Port ${Config.port} is already in use! Shutting down server...", 1)
        }

        socket.configureBlocking(true)
        println("Bound to ip ${socket.localAddress}.")
        Config.lock = true
        println("Creating fixed thread pool with ${Config.threadCount} threads.")
        runBlocking {
            launchWithThreadCount { startConsoleInput() }
            launchWithThreadCount {
                while (isActive) {
                    tickTimeMillis =
                        measureTimeMillis {
                            tick() // Run game tick
                        }
                    delay(maxOf(0L, (1000 / Config.tps) - tickTimeMillis))
                }
            }
            while (true) {
                val session = Session(SocketChannel(socket.accept()))
                sessions.add(session)
                println("Accepted $session.")
            }
        }
    }

    public fun stop() {
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
        exitProcess(0)
    }

    public fun tick(): Unit = runBlocking {
        sessions.removeIf { !it.isAlive }
        sessions.forEach { session ->
            launchWithThreadCount {
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
            Biome.load()
        }.let { println("Loaded ${Biome.list.size} biomes in ${it}ms.") }
        measureTimeMillis {
            Command.load()
        }.let { println("Loaded ${Command.list.size} commands in ${it}ms.") }
        measureTimeMillis {
            Dimension.load()
        }.let { println("Loaded ${Dimension.list.size} dimensions in ${it}ms.") }
        measureTimeMillis {
            Material.load()
        }.let { println("Loaded ${Material.list.size} materials in ${it}ms.") }
    }

}
