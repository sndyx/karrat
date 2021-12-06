/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.ClientboundPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeChatComponent

public class LoginDisconnectPacket(
    private val reason: ChatComponent,
) : ClientboundPacket {
    
    override val id: Int = 0x00
    
    override fun write(data: DynamicByteBuffer): Unit =
        data.writeChatComponent(reason)
    
}