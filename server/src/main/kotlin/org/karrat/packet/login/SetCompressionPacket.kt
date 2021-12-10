package org.karrat.packet.login

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeVarInt

public class SetCompressionPacket(
    public val compressionThreshold: Int,
) : ClientboundPacket {

    override val id: Int = 0x03

    override fun write(data: DynamicByteBuffer): Unit = data.run {
        writeVarInt(compressionThreshold)
    }

}