/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.serverbound.status

import org.karrat.network.INetHandler
import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.util.ByteBuffer

class PingPacket(data : ByteBuffer) : ServerboundPacket {
    
    val timestamp = data.readLong()
    
}