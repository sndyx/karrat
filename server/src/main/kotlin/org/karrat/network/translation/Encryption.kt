/*
 * Copyright © Karrat - 2021.
 */
@file:Suppress("Unused")

package org.karrat.network.translation

import org.karrat.Server
import org.karrat.network.Session
import org.karrat.network.handler.NetHandlerLogin
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import java.math.BigInteger
import java.security.*
import javax.crypto.*
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
        fatal("Key pair generation failed!")
    }
}

private fun createCipherInstance(algorithm: String, key: PrivateKey): Cipher {
    return runCatching {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key)
        cipher
    }.getOrElse {
        fatal(it)
    }
}

private fun decryptData(key: PrivateKey, bytes: ByteArray): ByteArray {
    return runCatching {
        createCipherInstance(key.algorithm, key).doFinal(bytes)
    }.getOrElse {
        fatal(it)
    }
}

public fun EncryptionResponsePacket.decodeSharedSecret(key: PrivateKey): SecretKey {
    return SecretKeySpec(decryptData(key, sharedSecret), "AES")
}

public fun EncryptionResponsePacket.decodeVerificationToken(key: PrivateKey): ByteArray {
    return decryptData(key, verifyToken)
}

internal fun NetHandlerLogin.generateAESInstance(opMode: Int, key: Key): Cipher {
    return runCatching {
        val var2 = Cipher.getInstance("AES/CFB8/NoPadding")
        var2.init(opMode, key, IvParameterSpec(key.encoded))
        var2
    }.getOrElse {
        fatal("AES creation failed!")
    }
}

internal fun NetHandlerLogin.getServerIdHash(serverId: String, publicKey: PublicKey, secretKey: SecretKey): String {
    return runCatching {
        BigInteger(
            digestOperation(
            serverId.toByteArray(Charsets.ISO_8859_1), secretKey.encoded, publicKey.encoded
        )
        ).toString(16)
    }.getOrElse {
        fatal("Digest creation failed!")
    }
}

private fun digestOperation(vararg hashed: ByteArray): ByteArray? {
    return runCatching {
        val digest = MessageDigest.getInstance("SHA-1")
        for (element in hashed) {
            digest.update(element)
        }
        digest.digest()
    }.getOrElse {
        fatal("Digest creation failed!")
    }
}