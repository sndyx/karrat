/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.status

import org.karrat.packet.Packet
import org.karrat.packet.Serverbound
import org.karrat.util.ByteBuffer


@Serverbound
class PingPacket(val timestamp: Long) : Packet() {
    
    override val id = 0x01
    
    constructor(data: ByteBuffer) : this(
        timestamp = data.readLong()
    )
    
    override fun write(data: ByteBuffer) = data.writeLong(timestamp)
    
}