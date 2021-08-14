/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.*

class EncryptionRequestPacket(
    private val serverId: String,
    private val publicKey: ByteArray,
    private val verifyToken: ByteArray
    ) : ClientboundPacket {
    
    override val id = 0x01
    
    override fun write(data: ByteBuffer) = data.run {
        writeString(serverId)
        writePrefixed(publicKey)
        writePrefixed(verifyToken)
    }
    
}