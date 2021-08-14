/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.serverbound.status.PingPacket
import org.karrat.packet.serverbound.status.StatusRequestPacket
import org.karrat.packet.clientbound.status.PongPacket
import org.karrat.packet.clientbound.status.StatusResponsePacket
import org.karrat.util.ByteBuffer

open class NetHandlerStatus(val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> error { "Invalid packet id $id in state handshake." }
    }
    
    fun handlePingPacket(packet: PingPacket) =
        session.send(PongPacket(packet.timestamp))
    
    fun handleStatusRequestPacket() {
        session.send(StatusResponsePacket(
            """
                {
                    "version": {
                        "name": "Karrat 1.17.1",
                        "protocol": 756
                    },
                    "players": {
                        "max": 1,
                        "online": 1,
                        "sample": [
                            {
                                "name": "sndy",
                                "id": "bf8c0810-3dda-48ec-a573-43e162c0e79a"
                            }
                        ]
                    },
                    "description": {
                        "text": "Funny Gaming"
                    },
                    "favicon": "data:image/png;base64,<data>"
                }
            """
        ))
        // TODO: setup status request response.
    }
    
}