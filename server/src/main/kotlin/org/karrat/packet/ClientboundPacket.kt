/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

import org.karrat.struct.*

interface ClientboundPacket : Packet {
    
    val id: Int
    
    fun write(data: DynamicByteBuffer)
    
    fun toBytes(): ByteArray {
        val buffer = DynamicByteBuffer()
        buffer.writeVarInt(id)
        write(buffer)
        val packet = MutableByteBuffer(buffer.size + varSizeOf(buffer.size))
        packet.writeVarInt(buffer.size)
        packet.writeBytes(buffer.array())
        return packet.array()
    }
    
}