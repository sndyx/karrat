/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.serverbound.handshake.HandshakePacket
import org.karrat.util.ByteBuffer

class NetHandlerHandshake(private val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> HandshakePacket(data)
        else -> error { "Invalid packet id $id in state handshake." }
    }
    
    fun handleHandshakePacket(packet: HandshakePacket) {
        when (packet.nextState) {
            1 -> session.handler = NetHandlerStatus
            2 -> session.handler = NetHandlerLogin(session)
        }
    }
    
}