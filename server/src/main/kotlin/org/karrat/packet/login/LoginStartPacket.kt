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
    public lateinit var publicKeyArray: ByteArray
    public lateinit var signatureArray: ByteArray

    public var hasPlayerUUID: Boolean = data.readBoolean()
    public lateinit var playerUUID: Uuid

    init {
        if (hasSigData) {
            timestamp = data.readLong()
            publicKeyArray = data.readPrefixed()
            signatureArray = data.readPrefixed()
        }

        if (hasPlayerUUID) {
            playerUUID = data.readUuid()
        }
    }



}