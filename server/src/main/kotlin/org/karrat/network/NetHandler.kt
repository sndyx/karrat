/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer

public interface NetHandler {

    public fun read(id: Int, data: ByteBuffer): ServerboundPacket

    public fun process(packet: ServerboundPacket)

}