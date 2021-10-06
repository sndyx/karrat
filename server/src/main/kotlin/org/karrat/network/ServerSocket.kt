/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.karrat.Tickable
import org.karrat.internal.NetSocket
import org.karrat.server.info
import java.net.InetAddress
import kotlin.concurrent.thread

object ServerSocket : Tickable, CoroutineScope {
    
    override val coroutineContext = Dispatchers.IO
    
    private var sessions = mutableListOf<Session>()
    lateinit var socket: NetSocket
    
    fun start(port: Int) {
        socket = NetSocket(port, 0, InetAddress.getLocalHost())
        thread(name="ServerSocket") {
            info("Scanning for sessions.")
            while (true) sessions.add(Session(socket.accept()))
        }
    }
    
    fun stop() {
        info("Terminating sessions.")
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }
    
    override fun tick() {
        sessions.removeIf { !it.isAlive }
    }
    
}