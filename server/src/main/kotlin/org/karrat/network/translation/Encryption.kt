/*
 * Copyright © Karrat - 2022.
 */
@file:Suppress("Unused")

package org.karrat.network.translation

import org.karrat.Server
import org.karrat.network.Session
import org.karrat.network.handler.NetHandlerLogin
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.packet.login.LoginStartPacket
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

private fun createCipherInstance(algorithm: String, key: Key): Cipher {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(Cipher.DECRYPT_MODE, key)
    return cipher
}

public fun decryptData(key: Key, bytes: ByteArray): ByteArray =
    createCipherInstance(key.algorithm, key).doFinal(bytes)

public fun byteArrayToRSA(array: ByteArray): PublicKey =
    KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(array))

// TODO verify that algorithm is correct
public val LoginStartPacket.publicKey: PublicKey
    get() = byteArrayToRSA(publicKeyArray)


public fun EncryptionResponsePacket.getSharedSecret(key: PrivateKey): SecretKey {
    return SecretKeySpec(decryptData(key, sharedSecret), "AES")
}

internal fun NetHandlerLogin.checkDecryption(key: Key, encrypted: ByteArray, decrypted: ByteArray, lazyMessage: () -> Any) {
    check(decryptData(key, encrypted) contentEquals decrypted, lazyMessage)
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

internal fun NetHandlerLogin.getServerIdHash(serverId: String, publicKey: PublicKey, secretKey: SecretKey): ByteArray {
    return BigInteger(
        digestOperation(
            serverId.toByteArray(Charsets.ISO_8859_1), secretKey.encoded, publicKey.encoded
        )
    ).toByteArray()
}

private fun digestOperation(vararg hashed: ByteArray): ByteArray {
    return runCatching {
        val digest = MessageDigest.getInstance("SHA-1")
        hashed.forEach { digest.update(it) }
        digest.digest()
    }.getOrElse {
        error("Digest creation failed!")
    }
}