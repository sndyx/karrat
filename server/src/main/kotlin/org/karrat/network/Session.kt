/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.Packet
import java.net.Socket

class Session(private val socket: Socket) {
    
    val state = SessionState.HANDSHAKE
    val isAlive get() = !socket.isClosed
    val readChannel by lazy { socket.getInputStream().buffered() }
    val writeChannel by lazy { socket.getOutputStream() }
    
    fun send(packet: Packet) = writeChannel.write(packet.toBytes())
    
}