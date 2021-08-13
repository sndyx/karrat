/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Tickable
import org.karrat.packet.play.DisconnectPacket
import org.karrat.util.ChatComponent
import org.karrat.util.NetSocket
import kotlin.concurrent.thread

object ServerSocket : Tickable {
    
    private var sessions = mutableListOf<Session>()
    lateinit var socket: NetSocket
    
    fun start(port: Int) {
        socket = NetSocket(port)
        thread {
            while (true) sessions.add(Session(socket.accept()))
        }
    }
    
    val disconnect by lazy { DisconnectPacket(ChatComponent("Server shutting down.")) }
    
    fun stop() {
        sessions
            .filter { it.state == SessionState.PLAY }
            .forEach { it.send(disconnect) }
    }
    
    override fun tick() {
        sessions.removeIf { !it.isAlive }
    }
    
}