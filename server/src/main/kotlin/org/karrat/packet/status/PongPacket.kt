/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer

public class PongPacket(private val timestamp: Long) : ClientboundPacket {

    override val id: Int = 0x01

    override fun write(data: DynamicByteBuffer): Unit = data.writeLong(timestamp)

}