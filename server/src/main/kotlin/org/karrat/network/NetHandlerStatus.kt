/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.status.PingPacket
import org.karrat.packet.status.StatusRequestPacket
import org.karrat.util.ByteBuffer

object NetHandlerStatus : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> StatusRequestPacket
        0x01 -> PingPacket(data)
        else -> error { "Invalid packet id $id in state handshake." }
    }
    
}