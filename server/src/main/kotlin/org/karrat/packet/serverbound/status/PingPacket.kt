/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.serverbound.status

import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.struct.ByteBuffer

class PingPacket(data : ByteBuffer) : ServerboundPacket {
    
    val timestamp = data.readLong()
    
    override fun toString() = "PingPacket(timestamp=$timestamp)"
    
}