/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.*

@Clientbound
data class LoginSuccessPacket(
    val uuid: Uuid,
    val username: String
    ) : Packet() {
    
    override val id = 0x02
    
    constructor(data: ByteBuffer) : this(
        uuid = data.readUUID(),
        username = data.readString()
    )
    
    override fun write(data: ByteBuffer) = data.run {
        writeUUID(uuid)
        writeString(username)
    }
    
}