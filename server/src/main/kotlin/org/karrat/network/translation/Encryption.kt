/*
 * Copyright © Karrat - 2022.
 */
@file:Suppress("Unused")

package org.karrat.network.translation

import org.karrat.Server
import org.karrat.internal.decryptData
import org.karrat.network.Session
import org.karrat.network.handler.NetHandlerLogin
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.struct.ByteBuffer
import java.math.BigInteger
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal fun Session.cipher(buffer: ByteBuffer) {
    buffer.bytes = ciphers.first.update(buffer.bytes, 0, buffer.size)
}

internal fun Session.decipher(buffer: ByteBuffer) {
    buffer.bytes = ciphers.second.update(buffer.bytes, 0, buffer.size)
}

internal fun Server.generateKeyPair(): KeyPair {
    return runCatching {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPairGen.initialize(1024)
        keyPairGen.generateKeyPair()
    }.getOrElse {
        error("Key pair generation failed!")
    }
}

internal fun NetHandlerLogin.generateAESInstance(opMode: Int, key: Key): Cipher {
    return runCatching {
        val var2 = Cipher.getInstance("AES/CFB8/NoPadding")
        var2.init(opMode, key, IvParameterSpec(key.encoded))
        var2
    }.getOrElse {
        error("AES creation failed!")
    }
}

public fun EncryptionResponsePacket.getSharedSecret(key: PrivateKey): SecretKey {
    return SecretKeySpec(decryptData(key, sharedSecret), "AES")
}
