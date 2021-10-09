/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.network.Session
import org.karrat.network.SessionState
import org.karrat.network.state
import org.karrat.server.info
import java.net.InetAddress
import java.net.ServerSocket
import kotlin.concurrent.thread

object Server {
    
    var sessions = mutableListOf<Session>()
    lateinit var socket: ServerSocket
    
    fun start(port: Int) {
        socket = ServerSocket(port, 0, InetAddress.getLocalHost())
        thread(name="socket") {
            while (true) sessions.add(Session(socket.accept()))
        }
    }
    
    fun stop() {
        info("Terminating sessions.")
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }
    
    init {
        thread(name="tick") {
            while (true) sessions.forEach {
                if (!it.isAlive) sessions.remove(it)
                it.handle()
            }
        }
    }

}