/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login.clientbound

import org.karrat.packet.ClientboundPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeChatComponent

class LoginDisconnectPacket(private val reason: ChatComponent) : ClientboundPacket {
    
    override val id = 0x00
    
    override fun write(data: DynamicByteBuffer) = data.writeChatComponent(reason)
    
}