/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeString
import org.karrat.struct.writeVarInt

public class StatusResponsePacket(private val response: String) : ClientboundPacket {
    
    override val id: Int = 0x00
    
    override fun write(data: DynamicByteBuffer) {
        data.writeVarInt(response.length)
        data.writeString(response)
    }
    
}