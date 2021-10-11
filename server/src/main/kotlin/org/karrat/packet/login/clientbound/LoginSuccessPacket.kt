/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login.clientbound

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.Uuid
import org.karrat.struct.writeString
import org.karrat.struct.writeUuid

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