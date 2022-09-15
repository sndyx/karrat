/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.login

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.Message
import org.karrat.struct.writeMessage

public class LoginDisconnectPacket(
    private val reason: Message,
) : ClientboundPacket {

    override val id: Int = 0x00

    override fun write(data: DynamicByteBuffer): Unit =
        data.writeMessage(reason)

}