/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.handler

import org.karrat.Server
import org.karrat.event.StatusResponseEvent
import org.karrat.event.dispatchEvent
import org.karrat.network.NetHandler
import org.karrat.network.Session
import org.karrat.response.StatusResponse
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.status.PingPacket
import org.karrat.packet.status.PongPacket
import org.karrat.packet.status.StatusRequestPacket
import org.karrat.packet.status.StatusResponsePacket
import org.karrat.struct.ByteBuffer

public open class NetHandlerStatus(private val session: Session) : NetHandler {

    override fun read(id: Int, data: ByteBuffer): ServerboundPacket = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> error("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is PingPacket -> session.send(PongPacket(packet.timestamp))
        is StatusRequestPacket -> {
            val event = StatusResponseEvent(session, StatusResponse.default())
            if (!Server.dispatchEvent(event)) {
                session.send(StatusResponsePacket(event.response.compile().toString()))
            } else Unit // frick you kotlin!!!!
        }
        else -> error("Failed to handle packet: Invalid packet.")
    }

}