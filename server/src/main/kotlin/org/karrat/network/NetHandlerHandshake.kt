/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.ServerboundPacket
import org.karrat.packet.handshake.HandshakePacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer

open class NetHandlerHandshake(private val session: Session) : NetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> HandshakePacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }
    
    override fun process(packet: ServerboundPacket) = when (packet) {
            is HandshakePacket -> when (packet.nextState) {
                1 -> session.netHandler = NetHandlerStatus(session)
                2 -> session.netHandler = NetHandlerLogin(session)
                else -> fatal { "Invalid handshake packet state to be handled." }
            }
            else -> fatal("Invalid packet to be handled.")
        }

}