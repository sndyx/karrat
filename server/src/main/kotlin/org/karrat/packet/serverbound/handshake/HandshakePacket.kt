/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.serverbound.handshake

import org.karrat.struct.ByteBuffer
import org.karrat.struct.readString
import org.karrat.struct.readUShort
import org.karrat.struct.readVarInt
import org.karrat.packet.serverbound.ServerboundPacket

class HandshakePacket(data: ByteBuffer) : ServerboundPacket {
    
    val protocol = data.readVarInt()
    val address = data.readString()
    val port = data.readUShort()
    val nextState = data.readVarInt()
    
    override fun toString() = "HandshakePacket(protocol=$protocol, address=$address, port=$port, nextState=$nextState)"
    
}