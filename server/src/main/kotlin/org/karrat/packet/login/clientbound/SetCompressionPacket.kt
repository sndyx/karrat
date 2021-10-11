package org.karrat.packet.login.clientbound

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeVarInt

class SetCompressionPacket(val compressionThreshold : Int) : ClientboundPacket {

    override val id = 0x03

    override fun write(data: DynamicByteBuffer) = data.run {
        writeVarInt(compressionThreshold)
    }


}