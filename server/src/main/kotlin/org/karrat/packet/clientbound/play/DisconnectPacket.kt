/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.play

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent
import org.karrat.util.writeChatComponent

class DisconnectPacket(private val message: ChatComponent) : ClientboundPacket {
    
    override val id = 0x1A
    
    override fun write(data: ByteBuffer) = data.writeChatComponent(message)
    
}