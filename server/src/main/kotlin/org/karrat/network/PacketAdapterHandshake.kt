/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.handshake.HandshakePacket
import org.karrat.util.ByteBuffer

class PacketAdapterHandshake : IPacketAdapter {
    
    override fun read(id: Int, data: ByteBuffer) = when (id) {
        0x00 -> HandshakePacket(data)
        else -> error { "Invalid packet id $id in state handshake." }
    }
    
}