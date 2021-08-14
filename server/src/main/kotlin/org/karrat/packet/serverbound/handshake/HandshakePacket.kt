/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.serverbound.handshake

import org.karrat.network.INetHandler
import org.karrat.network.NetHandlerHandshake
import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.util.*

class HandshakePacket(data: ByteBuffer) : ServerboundPacket {
    
    val protocol = data.readVarInt()
    val address = data.readString()
    val port = data.readUShort()
    val nextState = data.readVarInt()
    
}