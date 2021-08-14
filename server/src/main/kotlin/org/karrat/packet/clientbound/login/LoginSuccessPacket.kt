/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.login

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.*

class LoginSuccessPacket(
    private val uuid: Uuid,
    private val username: String
    ) : ClientboundPacket {
    
    override val id = 0x02
    
    override fun write(data: ByteBuffer) = data.run {
        writeUUID(uuid)
        writeString(username)
    }
    
}