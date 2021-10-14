/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer

public class NetHandlerPlay : NetHandler {
    override fun read(id: Int, data: ByteBuffer): ServerboundPacket {
        TODO("Not yet implemented")
    }

    override fun process(packet: ServerboundPacket) {
        TODO("Not yet implemented")
    }
}