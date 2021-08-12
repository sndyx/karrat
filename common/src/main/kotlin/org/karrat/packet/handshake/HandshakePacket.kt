/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.handshake

import org.karrat.packet.*
import org.karrat.util.*

@Serverbound
data class HandshakePacket(
    val protocol: Int,
    val address: String,
    val port: UShort,
    val state: Int
    ) : Packet() {
    
    override val id = 0x00
    
    constructor(data: ByteBuffer) : this(
        protocol = data.readVarInt(),
        address = data.readString(),
        port = data.readUShort(),
        state = data.readVarInt()
    )
    
    override fun write(data: ByteBuffer) = data.run {
        writeVarInt(protocol)
        writeString(address)
        writeUShort(port)
        writeVarInt(state)
    }
    
}