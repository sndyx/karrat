package org.karrat.utils

import org.karrat.server.fatal
import java.io.UnsupportedEncodingException
import java.security.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

//Literally minecraft's :)
object CryptManager {

    fun getServerIdHash(serverId: String, publicKey: PublicKey, secretKey: SecretKey): ByteArray? {
        return try {
            digestOperation(
                "SHA-1",
                serverId.toByteArray(Charsets.ISO_8859_1), secretKey.encoded, publicKey.encoded
            )
        } catch (e : UnsupportedEncodingException) {
            fatal("Digest creation failed!")
        }
    }

    private fun digestOperation(hashAlgorithm: String, vararg hashed: ByteArray): ByteArray? {
        return try {
            val digest = MessageDigest.getInstance(hashAlgorithm)
            for (element in hashed) {
                digest.update(element)
            }
            digest.digest()
        } catch (e : NoSuchAlgorithmException) {
            fatal("Digest creation failed!")
        }
    }

    fun decryptSharedKey(key: PrivateKey, bytes: ByteArray): SecretKey {
        return SecretKeySpec(decryptData(key, bytes), "AES")
    }

    fun decryptData(key: PrivateKey, bytes: ByteArray): ByteArray {
        return cipherOperation(2, key, bytes)
    }

    private fun cipherOperation(cipherMode: Int, key: PrivateKey, bytes: ByteArray): ByteArray {
        try {
            return createTheCipherInstance(cipherMode, key.getAlgorithm(), key).doFinal(bytes)
        } catch (e : IllegalBlockSizeException) {
            e .printStackTrace()
        } catch (e : BadPaddingException) {
            e .printStackTrace()
        }
        fatal("Cipher data failed!")
    }

    private fun createTheCipherInstance(cipherMode: Int, algorithm: String, key: PrivateKey): Cipher {
        try {
            val cipher = Cipher.getInstance(algorithm)
            cipher.init(cipherMode, key, IvParameterSpec(key.encoded))
            return cipher
        } catch (e : InvalidKeyException) {
            e.printStackTrace()
        } catch (e : NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e : NoSuchPaddingException) {
            e.printStackTrace()
        }
        fatal("Cipher creation failed!")
    }

    fun generateAESInstance(opmode: Int, key: Key): Cipher {
        return try {
            val var2 = Cipher.getInstance("AES/CFB8/NoPadding")
            var2.init(opmode, key, IvParameterSpec(key.encoded))
            var2
        } catch (e : GeneralSecurityException) {
            e.printStackTrace()
            fatal("AES creation failed!")
        }
    }
}