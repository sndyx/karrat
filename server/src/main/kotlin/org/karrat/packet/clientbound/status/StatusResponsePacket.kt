/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.status

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeString

class StatusResponsePacket(private val response: String) : ClientboundPacket {
    
    override val id = 0x00
    
    override fun write(data: DynamicByteBuffer) = data.writeString(response)
    
}