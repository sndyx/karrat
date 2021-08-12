package org.karrat.packet

import org.karrat.util.ByteBuffer
import org.karrat.util.writeVarInt

abstract class Packet {
    
    abstract val id: Int
    
    fun toBytes(): ByteArray {
        val buffer = ByteBuffer()
        buffer.writeVarInt(id)
        write(buffer)
        val packet = ByteBuffer()
        packet.writeVarInt(buffer.size)
        packet.writeBytes(buffer.array())
        return packet.array()
    }
    
    internal abstract fun write(data: ByteBuffer)
    
}