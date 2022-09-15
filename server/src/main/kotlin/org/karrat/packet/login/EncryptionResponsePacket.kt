/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.login

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readPrefixed

public class EncryptionResponsePacket(data: ByteBuffer) : ServerboundPacket {

    public val sharedSecret: ByteArray = data.readPrefixed()
    public val hasVerifyToken: Boolean = data.readBoolean()

    public lateinit var verifyToken: ByteArray

    public var salt: Long = 0
    public lateinit var tokenSignature: ByteArray

    init {
        if (hasVerifyToken) { // TODO cleaner branching/optional support
            verifyToken = data.readPrefixed()
        } else {
            salt = data.readLong()
            tokenSignature = data.readPrefixed()
        }
    }
}