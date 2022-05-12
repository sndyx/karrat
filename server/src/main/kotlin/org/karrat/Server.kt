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
import org.karrat.plugin.LoadedPlugin
import org.karrat.plugin.loadPlugins
import org.karrat.server.console.startConsoleInput
import org.karrat.server.console.setConsoleOutput
import org.karrat.server.loadResources
import org.karrat.struct.id
import org.karrat.world.Dimension
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

public object Server : CoroutineScope {
    
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    override val coroutineContext: CoroutineContext =
        newFixedThreadPoolContext(Config.threadCount, "worker-thread")
    
    public lateinit var socket: ServerSocketChannel
    public var auth: AuthServer = AuthServer()
    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    
    public var worlds: MutableList<World> = mutableListOf()
    public var commands: MutableList<Command> = mutableListOf()
    public var sessions: MutableList<Session> = mutableListOf()
    public var plugins: MutableList<LoadedPlugin> = mutableListOf()
    public val players: List<Player> get() = worlds.flatMap { it.players }
    
    internal var tickTimeMillis: Long = 0L
    
    public fun start(): Unit = runBlocking {
        Config.lock = true
        setConsoleOutput()
        println("Server starting.")
        if (isFirstRun) {
            genServerFiles()
            worlds.add(World(id("minecraft:main"), Dimension.Overworld, 0))
        }
        if (!Config.isDevEnvironment) {
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
        println("Creating fixed thread pool with ${Config.threadCount} threads.")
        loadPlugins()
        launch { startConsoleInput() }
        launch {
            while (isActive) {
                tickTimeMillis =
                    measureTimeMillis {
                        tick() // Run game tick
                    }
                delay(maxOf(0L, (1000 / Config.tickrate) - tickTimeMillis))
            }
        }
        launch {
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

    public fun tick() {
        sessions.removeAll { !it.isAlive }
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
    
}
