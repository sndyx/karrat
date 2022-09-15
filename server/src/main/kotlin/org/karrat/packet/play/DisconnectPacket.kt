/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.play

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.Message
import org.karrat.struct.writeMessage

public class DisconnectPacket(
    private val message: Message,
) : ClientboundPacket {

    override val id: Int = 0x1A

    override fun write(data: DynamicByteBuffer): Unit =
        data.writeMessage(message)

}
