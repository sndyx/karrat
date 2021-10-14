/*
 * Copyright © Karrat - 2021.
 */
@file:Suppress("Unused")

package org.karrat.network

import org.karrat.Server
import org.karrat.packet.login.serverbound.EncryptionResponsePacket
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
    return try {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPairGen.initialize(1024)
        keyPairGen.generateKeyPair()
    } catch (e : NoSuchAlgorithmException) {
        e.printStackTrace()
        fatal("Key pair generation failed!")
    }
}

private fun cipherOperation(key: PrivateKey, bytes: ByteArray): ByteArray {
    return try {
        createCipherInstance(key.algorithm, key).doFinal(bytes)
    } catch (e: Exception) {
        e.printStackTrace()
        fatal("Cipher data failed!")
    }
}

private fun createCipherInstance(algorithm: String, key: PrivateKey): Cipher {
    return try {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(2, key)
        return cipher
    } catch (e: Exception) {
        e.printStackTrace()
        fatal("Cipher creation failed!")
    }
}

private fun decryptData(key: PrivateKey, bytes: ByteArray): ByteArray {
    return cipherOperation(key, bytes)
}

public fun EncryptionResponsePacket.decodeSharedSecret(key: PrivateKey): SecretKey {
    return SecretKeySpec(decryptData(key, sharedSecret), "AES")
}

public fun EncryptionResponsePacket.decodeVerificationToken(privateKey: PrivateKey): ByteArray {
    return decryptData(privateKey, verifyToken)
}

internal fun NetHandlerLogin.generateAESInstance(opMode: Int, key: Key): Cipher {
    return try {
        val instance = Cipher.getInstance("AES/CFB8/NoPadding")
        instance.init(opMode, key, IvParameterSpec(key.encoded))
        instance
    } catch (e : Exception) {
        e.printStackTrace()
        fatal("AES creation failed!")
    }
}

internal fun NetHandlerLogin.getServerIdHash(serverId: String, publicKey: PublicKey, secretKey: SecretKey): String {
    return try {
        BigInteger(digestOperation(
            serverId.toByteArray(Charsets.ISO_8859_1), secretKey.encoded, publicKey.encoded
        )).toString(16)
    } catch (e : Exception) {
        fatal("Digest creation failed!")
    }
}

private fun digestOperation(vararg hashed: ByteArray): ByteArray? {
    return try {
        val digest = MessageDigest.getInstance("SHA-1")
        for (element in hashed) {
            digest.update(element)
        }
        digest.digest()
    } catch (e : NoSuchAlgorithmException) {
        fatal("Digest creation failed, No such algorithm of SHA-1?")
    }
}