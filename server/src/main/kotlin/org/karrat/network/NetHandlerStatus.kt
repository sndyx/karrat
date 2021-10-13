/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Server
import org.karrat.event.StatusResponseEvent
import org.karrat.event.dispatchEvent
import org.karrat.packet.status.PingPacket
import org.karrat.packet.status.StatusRequestPacket
import org.karrat.packet.status.PongPacket
import org.karrat.packet.status.StatusResponsePacket
import org.karrat.packet.ServerboundPacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer

public open class NetHandlerStatus(public val session: Session) : NetHandler {
    
    override fun read(id: Int, data: ByteBuffer): ServerboundPacket = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is PingPacket -> session.send(PongPacket(packet.timestamp))
        is StatusRequestPacket -> {
            val event = StatusResponseEvent(session, StatusResponse.default())
            if (!Server.dispatchEvent(event)) {
                session.send(StatusResponsePacket(event.response.compile().toString()))
            }
            else Unit // frick you kotlin!!!!
        }
        else -> fatal("Failed to handle packet: Invalid packet.")

    }
}