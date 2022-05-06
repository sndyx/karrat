/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import kotlinx.coroutines.*
import org.karrat.command.Command
import org.karrat.configuration.*
import org.karrat.configuration.eulaPrompt
import org.karrat.configuration.genServerFiles
import org.karrat.entity.Player
import org.karrat.internal.exitProcessWithMessage
import org.karrat.network.*
import org.karrat.network.translation.generateKeyPair
import org.karrat.play.Material
import org.karrat.server.console.registerConsoleInput
import org.karrat.server.console.registerConsoleOutput
import org.karrat.configuration.launchInThreadPool
import org.karrat.struct.id
import org.karrat.world.Biome
import org.karrat.world.Dimension
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

public object Server {
    
    public lateinit var socket: ServerSocketChannel
    public var auth: AuthServer = AuthServer()
    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    
    public var worlds: MutableList<World> = mutableListOf()
    public var commands: MutableList<Command> = mutableListOf()
    public var sessions: MutableList<Session> = mutableListOf()
    public val players: List<Player> get() = worlds.flatMap { it.players }
    
    internal var tickTimeMillis: Long = 0L
    
    public fun start(): Unit = runBlocking {
        Config.lock = true

        registerConsoleOutput()

        println("Server starting.")


        genServerFiles()

        if (!Config.isDevEnvironment) {
            if (isFirstRun) {
                worlds.add(World(id("minecraft:main"), Dimension.Overworld, 0)) // make this optional somehow
            }

            eulaPrompt()
        }

        loadResources()

        socket = ServerSocketChannel.open()
        runCatching {
            socket.bind(InetSocketAddress(InetAddress.getLocalHost(), Config.port))
        }.onFailure {
            exitProcessWithMessage("Port ${Config.port} is already in use! Shutting down server...", 1)
        }
        socket.configureBlocking(false)
        println("Bound to ip ${socket.localAddress}.")

        registerConsoleInput()

        launchInThreadPool {
            while (isActive) {
                tickTimeMillis =
                    measureTimeMillis {
                        tick() // Run game tick
                    }
                delay(maxOf(0L, (1000 / Config.tps) - tickTimeMillis))
            }
        }
        launchInThreadPool {
            while (isActive) {
                @Suppress("BlockingMethodInNonBlockingContext") // you dummy you moron you IDIOT !!!! its called NONBLOCKING IO for a reason !!!!!
                socket.accept()?.let {
                    it.configureBlocking(false)
                    val session = Session(SocketChannel(it))
                    sessions.add(session)
                    println("Accepted $session")
                }
                delay(1000L)
            }
        }
    }

    public fun stop() {
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
        exitProcess(0)
    }

    public suspend fun tick(): Unit = coroutineScope {
        sessions.removeAll { !it.isAlive }
        sessions.forEach { session ->
            launchInThreadPool {
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
