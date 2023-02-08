/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.packet.status

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeString

public class StatusResponsePacket(private val response: String) : ClientboundPacket {

    override val id: Int = 0x00

    override fun write(data: DynamicByteBuffer): Unit = data.writeString(response)

}