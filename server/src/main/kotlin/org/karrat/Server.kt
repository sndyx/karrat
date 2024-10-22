/*
 * Copyright © Karrat - 2023.
 */
@file:Suppress("BlockingMethodInNonBlockingContext")
// kotlin when """"""""blocking"""""""" method:
// https://www.youtube.com/watch?v=lmbJP1yObZc

package org.karrat

import kotlinx.coroutines.*
import org.karrat.command.Command
import org.karrat.configuration.*
import org.karrat.configuration.eulaPrompt
import org.karrat.configuration.genServerFiles
import org.karrat.entity.Player
import org.karrat.internal.exitProcessWithMessage
import org.karrat.network.*
import org.karrat.network.auth.AuthServer
import org.karrat.network.translation.generateKeyPair
import org.karrat.plugin.Plugin
import org.karrat.server.console.startConsoleInput
import org.karrat.server.console.setConsoleOutput
import org.karrat.server.loadResources
import org.karrat.struct.id
import org.karrat.world.Dimension
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

public object Server : CoroutineScope {
    
    @OptIn(DelicateCoroutinesApi::class)
    override val coroutineContext: CoroutineContext =
        newFixedThreadPoolContext(Config.threadCount, "worker-thread")
    
    public lateinit var socket: ServerSocketChannel
    public var auth: AuthServer = AuthServer()
    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    
    public var worlds: MutableList<World> = mutableListOf()
    public var sessions: MutableList<Session> = mutableListOf()
    public val players: List<Player> get() = worlds.flatMap { it.players }
    public val commands: List<Command> get() = Command.list
    public val plugins: List<Plugin> get() = Plugin.list

    internal var tickTimeMillis: Long = 0L
    
    public fun start(): Unit = runBlocking {
        Config.lock = true
        setConsoleOutput()
        println("Server starting.")

        // DEBUG
        worlds.add(World(id("minecraft:main"), Dimension.Overworld, 0))

        if (isFirstRun) {
            genServerFiles()
        }
        if (!Config.isDevEnvironment) {
            eulaPrompt()
        }
        socket = ServerSocketChannel.open()
        runCatching {
            socket.bind(InetSocketAddress(Config.port))
        }.onFailure {
            exitProcessWithMessage("Port ${Config.port} is already in use! Shutting down server...", 1)
        }
        socket.configureBlocking(false)
        println("Bound to ip ${socket.localAddress}.")
        println("Creating fixed thread pool with ${Config.threadCount} threads.")
        loadResources()
        launch { startConsoleInput() }
        println("Server started.")
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
                // you dummy you moron you IDIOT !!!! its NONBLOCKING IO !!!!!
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
            .filter { it.state == SessionState.Play }
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
