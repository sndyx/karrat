/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.World
import org.karrat.entity.FakePlayer
import org.karrat.entity.Player
import org.karrat.event.StatusResponseEvent
import org.karrat.event.dispatchEvent
import org.karrat.packet.serverbound.status.PingPacket
import org.karrat.packet.serverbound.status.StatusRequestPacket
import org.karrat.packet.clientbound.status.PongPacket
import org.karrat.packet.clientbound.status.StatusResponsePacket
import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.play.ChatComponent
import org.karrat.play.Location
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.Uuid
import java.util.*

open class NetHandlerStatus(val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket) = when (packet) {
        is PingPacket -> session.send(PongPacket(packet.timestamp))
        is StatusRequestPacket -> {
            //This is all default code that is semi horrid
            val event = StatusResponseEvent(session, StatusResponse(
                "Karrat 1.17.1",
                756,
                1,
                1,
                Arrays.asList(FakePlayer(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"), "sndy",  Location(World(("TODO")), 0.0, 0.0, 0.0))),
                ChatComponent("Funny Gaming"),
                DynamicByteBuffer()
            ))

            dispatchEvent(event)
            session.send(StatusResponsePacket(event.response.toString()))
        }
        else -> fatal("Failed to handle packet: Invalid packet.")

    }
}