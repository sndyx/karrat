/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.packet.login

import org.karrat.packet.ClientboundPacket
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.writePrefixed
import org.karrat.struct.writeString

public class EncryptionRequestPacket(
    private val serverId: String,
    private val publicKey: ByteArray,
    private val verifyToken: ByteArray,
) : ClientboundPacket {

    override val id: Int = 0x01

    override fun write(data: DynamicByteBuffer): Unit = data.run {
        writeString(serverId)
        writePrefixed(publicKey)
        writePrefixed(verifyToken)
    }

    override fun toString(): String = "EncryptionRequestPacket(" +
            "serverId=$serverId, publicKey=$publicKey," +
            " verifyToken=$verifyToken)"

}