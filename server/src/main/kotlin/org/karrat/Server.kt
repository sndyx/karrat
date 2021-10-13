/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.network.Session
import org.karrat.network.SessionState
import org.karrat.network.generateKeyPair
import org.karrat.network.state
import org.karrat.server.info
import java.net.InetAddress
import java.net.ServerSocket
import java.security.KeyPair
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

public object Server {
    
    public var sessions: MutableList<Session> = mutableListOf()
    public lateinit var socket: ServerSocket
    
    internal val keyPair: KeyPair by lazy { generateKeyPair() }
    internal var tickTimeMillis: Long = 0L
    
    public fun start(port: Int) {
        info("Server starting.")
        socket = ServerSocket(port, 0, InetAddress.getLocalHost())
        info("Bound to ip ${socket.inetAddress.hostAddress} on port $port.")
        thread(name="socket") {
            while (true) {
                val session = Session(socket.accept())
                sessions.add(session)
                info("Accepted session @${session.socket.inetAddress.hostAddress}.")
            }
        }
        thread(name="tick") {
            while (true) {
                tickTimeMillis =
                    measureTimeMillis {
                        tick()
                    }
                Thread.sleep(maxOf(0L, 50L - tickTimeMillis))
            }
        }
    }
    
    public fun stop() {
        info("Terminating sessions.")
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }
    
    public fun tick() {
        sessions.forEach {
            if (!it.isAlive) sessions.remove(it)
            it.handle()
        }
    }

}