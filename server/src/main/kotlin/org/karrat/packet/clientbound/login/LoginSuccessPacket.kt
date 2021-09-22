/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.login

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.DynamicByteBuffer
import org.karrat.util.Uuid
import org.karrat.util.writeString
import org.karrat.util.writeUuid

class LoginSuccessPacket(
    private val uuid: Uuid,
    private val username: String
    ) : ClientboundPacket {
    
    override val id = 0x02
    
    override fun write(data: DynamicByteBuffer) = data.run {
        writeUuid(uuid)
        writeString(username)
    }
    
}