/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer

interface INetHandler {
    
    fun read(id: Int, data: ByteBuffer): ServerboundPacket

    fun process(packet : ServerboundPacket)
    
}