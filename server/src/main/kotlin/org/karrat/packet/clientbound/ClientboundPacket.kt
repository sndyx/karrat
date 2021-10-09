/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound

import org.karrat.struct.DynamicByteBuffer

interface ClientboundPacket {
    override fun toString() : String
    
    val id: Int
    
    fun write(data: DynamicByteBuffer)
    
}