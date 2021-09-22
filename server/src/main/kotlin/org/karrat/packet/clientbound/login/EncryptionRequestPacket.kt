/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.clientbound.login

import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.util.DynamicByteBuffer
import org.karrat.util.writePrefixed
import org.karrat.util.writeString

class EncryptionRequestPacket(
    private val serverId: String,
    private val publicKey: ByteArray,
    private val verifyToken: ByteArray
    ) : ClientboundPacket {
    
    override val id = 0x01
    
    override fun write(data: DynamicByteBuffer) = data.run {
        writeString(serverId)
        writePrefixed(publicKey)
        writePrefixed(verifyToken)
    }
    
}