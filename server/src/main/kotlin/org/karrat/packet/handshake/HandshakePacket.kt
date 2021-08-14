/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.handshake

import org.karrat.network.INetHandler
import org.karrat.network.NetHandlerHandshake
import org.karrat.packet.*
import org.karrat.util.*

class HandshakePacket(data: ByteBuffer) : ServerboundPacket {
    
    val protocol = data.readVarInt()
    val address = data.readString()
    val port = data.readUShort()
    val nextState = data.readVarInt()
    
    override fun process(handler: INetHandler) {
        check(handler is NetHandlerHandshake)
        handler.handleHandshakePacket(this)
    }
    
}