/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.karrat.entity.Player
import org.karrat.network.Session
import org.karrat.network.SessionState
import org.karrat.network.translation.generateKeyPair
import org.karrat.network.state
import org.karrat.server.info
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.security.KeyPair
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

public object Server {
    public var worlds : MutableList<World> = mutableListOf()

    public val players: Set<Player>
        get() {
            val result : MutableSet<Player> = mutableSetOf()

            worlds.forEach {
                result.addAll(it.players)
            }

            return result
        }
    
    private var sessions: MutableList<Session> = mutableListOf()
    public lateinit var socket: ServerSocketChannel
    
    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    internal var tickTimeMillis: Long = 0L
    
    public fun start(port: Int) {
        info("Server starting.")
        socket = ServerSocketChannel.open()
        socket.bind(InetSocketAddress(InetAddress.getLocalHost(), port))
        socket.configureBlocking(true)
        info("Bound to ip ${socket.localAddress} on port $port.")
        thread(name = "socket") {
            while (true) {
                val session = Session(socket.accept())
                sessions.add(session)
                info("Accepted $session.")
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
        info("Terminating sessions.")
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }
    
    public fun tick(): Unit = runBlocking {
        sessions.removeAll {
            var remove = false
            launch {
                if (!it.isAlive) {
                    remove = true
                } else {
                    try {
                        //TODO
                        /*
                         - Test if remove working (I tested something equivalent to
                         runBlocking {
                            var remove = false
                            launch {
                                remove = true
                            }

                            println(remove)
                         }

                         and got false)

                         - Make sure if a fix is required that the fact the socket channel is nonblocking is a non problem
                         )
                         */
                        it.handle()
                    } catch (e: Exception) {
                        info("$it disconnected; internal error: ${e.message}")
                        it.disconnect("Internal error: ${e.message}")
                        remove = true // Connection Reset by peer, along with others
                    }
                    remove = false
                }
            }
            return@removeAll remove
        }
    }

}