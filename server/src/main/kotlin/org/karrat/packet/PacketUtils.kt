/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.ByteBuffer
import org.karrat.util.writeVarInt

fun ClientboundPacket.toBytes(): ByteArray {
    val buffer = ByteBuffer()
    buffer.writeVarInt(id)
    write(buffer)
    val packet = ByteBuffer()
    packet.writeVarInt(buffer.size)
    packet.writeBytes(buffer.array())
    return packet.array()
}