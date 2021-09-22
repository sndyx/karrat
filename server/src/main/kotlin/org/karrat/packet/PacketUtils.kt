/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.*

fun ClientboundPacket.toBytes(): ByteArray {
    val buffer = DynamicByteBuffer()
    buffer.writeVarInt(id)
    write(buffer)
    val packet = MutableByteBuffer(buffer.size + varSizeOf(buffer.size))
    packet.writeVarInt(buffer.size)
    packet.writeBytes(buffer.array())
    return packet.array()
}