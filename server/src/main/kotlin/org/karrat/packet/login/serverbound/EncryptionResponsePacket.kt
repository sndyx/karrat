/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login.serverbound

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readPrefixed
import org.karrat.utils.CryptManager
import java.security.PrivateKey
import javax.crypto.SecretKey

class EncryptionResponsePacket (data: ByteBuffer) : ServerboundPacket {
    
    val sharedSecretBytes: ByteArray = data.readPrefixed()
    val verifyTokenBytes: ByteArray = data.readPrefixed()

    fun decodeSharedSecret(key: PrivateKey): SecretKey {
        return CryptManager.decryptSharedKey(key, sharedSecretBytes);
    }

    fun decodeVerificationToken(privateKey: PrivateKey): ByteArray {
        return CryptManager.decryptData(privateKey, verifyTokenBytes)
    }
}