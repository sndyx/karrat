/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer

class PongPacket(private val timestamp: Long) : ClientboundPacket {
    
    override val id = 0x01
    
    override fun write(data: DynamicByteBuffer) = data.writeLong(timestamp)
    
}