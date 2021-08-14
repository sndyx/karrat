/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.util.ByteBuffer
import org.karrat.util.writeString

class StatusResponsePacket(private val response: String) : ClientboundPacket {
    
    override val id = 0x00
    
    override fun write(data: ByteBuffer) = data.writeString(response)
    
}