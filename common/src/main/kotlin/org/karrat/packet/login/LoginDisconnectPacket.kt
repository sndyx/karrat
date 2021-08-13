/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.ChatComponent
import org.karrat.util.ByteBuffer
import org.karrat.util.readChatComponent
import org.karrat.util.writeChatComponent

@Clientbound
data class LoginDisconnectPacket(
    val reason: ChatComponent
    ) : Packet() {
    
    override val id = 0x00
    
    constructor(data: ByteBuffer) : this(
        reason = data.readChatComponent()
    )
    
    override fun write(data: ByteBuffer) = data.writeChatComponent(reason)
    
}