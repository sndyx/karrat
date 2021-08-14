/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play

import org.karrat.packet.Clientbound
import org.karrat.packet.Packet
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent
import org.karrat.util.readChatComponent
import org.karrat.util.writeChatComponent

@Clientbound
class DisconnectPacket(val message: ChatComponent) : Packet() {
    
    override val id = 0x1A
    
    constructor(data: ByteBuffer) : this(
        message = data.readChatComponent()
    )
    
    override fun write(data: ByteBuffer) = data.writeChatComponent(message)
    
}