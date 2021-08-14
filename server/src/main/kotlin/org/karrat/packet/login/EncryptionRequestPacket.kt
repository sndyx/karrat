/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.*

@Suppress("ArrayInDataClass")
@Clientbound
data class EncryptionRequestPacket(
    val serverId: String,
    val publicKey: ByteArray,
    val verifyToken: ByteArray
    ) : Packet() {
    
    override val id = 0x01
    
    constructor(data: ByteBuffer) : this(
        serverId = data.readString(),
        publicKey = data.readPrefixed(),
        verifyToken = data.readPrefixed()
    )
    
    override fun write(data: ByteBuffer) = data.run {
        writeString(serverId)
        writePrefixed(publicKey)
        writePrefixed(verifyToken)
    }
    
}