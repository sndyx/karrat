/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.network.INetHandler
import org.karrat.packet.ServerboundPacket
import org.karrat.util.ByteBuffer

class PingPacket(data: ByteBuffer) : ServerboundPacket {
    
    val timestamp = data.readLong()
    
    override fun process(handler: INetHandler) {
        TODO("Not yet implemented")
    }
    
}