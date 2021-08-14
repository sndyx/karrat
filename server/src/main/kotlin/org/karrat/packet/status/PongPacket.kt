/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.util.ByteBuffer

class PongPacket(private val timestamp: Long) : ClientboundPacket {
    
    override val id = 0x01
    
    override fun write(data: ByteBuffer) = data.writeLong(timestamp)
    
}