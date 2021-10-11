/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.event.StatusResponseEvent
import org.karrat.event.dispatchEvent
import org.karrat.packet.status.PingPacket
import org.karrat.packet.status.StatusRequestPacket
import org.karrat.packet.status.PongPacket
import org.karrat.packet.status.StatusResponsePacket
import org.karrat.packet.ServerboundPacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer

open class NetHandlerStatus(val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket) = when (packet) {
        is PingPacket -> session.send(PongPacket(packet.timestamp))
        is StatusRequestPacket -> {
            val event = StatusResponseEvent(session, StatusResponse.default())
            if (!dispatchEvent(event)) {
                session.send(StatusResponsePacket(event.response.compile().toString()))
            }
            else Unit // frick you kotlin!!!!
        }
        else -> fatal("Failed to handle packet: Invalid packet.")

    }
}