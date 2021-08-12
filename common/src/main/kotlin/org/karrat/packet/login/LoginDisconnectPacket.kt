package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.ChatComponent
import org.karrat.util.ByteBuffer
import org.karrat.util.readFormattedText
import org.karrat.util.writeFormattedText

@Clientbound
data class LoginDisconnectPacket(
    val reason: ChatComponent
    ) : Packet() {
    
    override val id = 0x00
    
    constructor(data: ByteBuffer) : this(
        reason = data.readFormattedText()
    )
    
    override fun write(data: ByteBuffer) = data.writeFormattedText(reason)
    
}