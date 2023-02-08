/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.packet.status

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer

public class PingPacket(data: ByteBuffer) : ServerboundPacket {

    public val timestamp: Long = data.readLong()

    override fun toString(): String = "PingPacket(timestamp=$timestamp)"

}