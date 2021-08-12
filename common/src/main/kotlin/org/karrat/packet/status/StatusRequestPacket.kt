package org.karrat.packet.status

import org.karrat.packet.Packet
import org.karrat.packet.Serverbound
import org.karrat.util.ByteBuffer

@Serverbound
class StatusRequestPacket : Packet() {
    
    override val id = 0x00
    
    override fun write(data: ByteBuffer) {  }
    
}