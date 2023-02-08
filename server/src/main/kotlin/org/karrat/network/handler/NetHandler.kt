/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.network.handler

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer

public interface NetHandler {

    public fun read(id: Int, data: ByteBuffer): ServerboundPacket

    public fun process(packet: ServerboundPacket)

}