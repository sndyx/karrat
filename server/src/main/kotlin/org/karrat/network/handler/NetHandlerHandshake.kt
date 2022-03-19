/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.handler

import org.karrat.network.NetHandler
import org.karrat.network.Session
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.handshake.HandshakePacket
import org.karrat.struct.ByteBuffer

public open class NetHandlerHandshake(private val session: Session) : NetHandler {

    override fun read(id: Int, data: ByteBuffer): ServerboundPacket =
        when (id) {
            0x00 -> HandshakePacket(data)
            else -> error("Invalid packet id $id in state handshake.")
        }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is HandshakePacket -> when (packet.nextState) {
            1 -> session.netHandler = NetHandlerStatus(session)
            2 -> session.netHandler = NetHandlerLogin(session)
            else -> error("Invalid handshake packet state to be handled.")
        }
        else -> error("Invalid packet to be handled.")
    }

}