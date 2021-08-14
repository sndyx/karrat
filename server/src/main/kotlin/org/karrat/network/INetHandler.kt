/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.util.ByteBuffer

interface INetHandler {
    
    fun read(id: Int, data: ByteBuffer): ServerboundPacket

}