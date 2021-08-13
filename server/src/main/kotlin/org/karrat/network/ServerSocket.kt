/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.karrat.Tickable
import org.karrat.util.NetSocket
import kotlin.concurrent.thread

object ServerSocket : Tickable, CoroutineScope {
    
    override val coroutineContext = Dispatchers.IO
    
    private var sessions = mutableListOf<Session>()
    lateinit var socket: NetSocket
    
    fun start(port: Int) {
        socket = NetSocket(port)
        thread {
            while (true) sessions.add(Session(socket.accept()))
        }
    }
    
    fun stop() {
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.disconnect("Server shutting down.") }
    }
    
    override fun tick() {
        sessions.removeIf { !it.isAlive }
    }
    
}