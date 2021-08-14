/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound

import org.karrat.util.ByteBuffer

interface ClientboundPacket {
    
    val id: Int
    
    fun write(data: ByteBuffer)
    
}