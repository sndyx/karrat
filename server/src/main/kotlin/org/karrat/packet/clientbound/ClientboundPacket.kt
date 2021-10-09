/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound

import org.karrat.packet.Packet
import org.karrat.struct.DynamicByteBuffer

interface ClientboundPacket : Packet {
    
    val id: Int
    
    fun write(data: DynamicByteBuffer)
    
}