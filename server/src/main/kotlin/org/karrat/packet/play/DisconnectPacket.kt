/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play

import org.karrat.packet.ClientboundPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writeChatComponent

public class DisconnectPacket(
    private val message: ChatComponent,
) : ClientboundPacket {
    
    override val id: Int = 0x1A
    
    override fun write(data: DynamicByteBuffer): Unit =
        data.writeChatComponent(message)
}
