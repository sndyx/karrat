/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.login

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.ChatComponent
import org.karrat.util.DynamicByteBuffer
import org.karrat.util.writeChatComponent

class LoginDisconnectPacket(private val reason: ChatComponent) : ClientboundPacket {
    
    override val id = 0x00
    
    override fun write(data: DynamicByteBuffer) = data.writeChatComponent(reason)
    
}