/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.ServerboundPacket
import org.karrat.util.ByteBuffer

open class NetHandlerLogin(val session: Session) : INetHandler {
    
    override fun read(id: Int, data: ByteBuffer): ServerboundPacket {
        TODO("")
    }
    
}