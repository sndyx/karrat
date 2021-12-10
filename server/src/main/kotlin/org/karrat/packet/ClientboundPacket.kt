/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

import org.karrat.struct.DynamicByteBuffer

public interface ClientboundPacket : Packet {

    public val id: Int

    public fun write(data: DynamicByteBuffer)

}