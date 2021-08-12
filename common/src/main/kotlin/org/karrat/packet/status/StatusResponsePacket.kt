package org.karrat.packet.status

import org.karrat.packet.Clientbound
import org.karrat.packet.Packet
import org.karrat.util.ByteBuffer
import org.karrat.util.readString
import org.karrat.util.writeString

@Clientbound
class StatusResponsePacket(val response: String) : Packet() {
    
    override val id = 0x00
    
    constructor(data: ByteBuffer) : this(
        response = data.readString()
    )
    
    override fun write(data: ByteBuffer) = data.writeString(response)
    
}