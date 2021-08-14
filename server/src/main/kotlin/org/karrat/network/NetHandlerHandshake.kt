/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.packet.serverbound.handshake.HandshakePacket
import org.karrat.util.ByteBuffer

open class NetHandlerHandshake(private val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> HandshakePacket(data)
        else -> error { "Invalid packet id $id in state handshake." }
    }
    
    override fun process(packet: ServerboundPacket) = when (packet) {
            is HandshakePacket -> when (packet.nextState) {
                1 -> session.handler = NetHandlerStatus(session)
                2 -> session.handler = NetHandlerLogin(session)
                else -> error { "Invalid handshake packet state to be handled." }
            }
            else -> error { "Invalid packet to be handled." }
        }

}