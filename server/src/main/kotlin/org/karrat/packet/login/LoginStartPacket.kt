/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.login

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.*

public class LoginStartPacket(data: ByteBuffer) : ServerboundPacket {

    public val username: String = data.readString()

    // Optional
    public val hasSigData: Boolean = data.readBoolean()
    public var timestamp: Long = 0
    public lateinit var publicKey: ByteArray
    public lateinit var signature: ByteArray

    init {
        if (hasSigData) {
            timestamp = data.readLong()
            publicKey = data.readPrefixed()
            signature = data.readPrefixed()
        }
    }

    public var hasPlayerUUID: Boolean = data.readBoolean()
    public lateinit var playerUUID: Uuid

    init {
        if (hasPlayerUUID) {
            playerUUID = data.readUuid()
        }
    }



}