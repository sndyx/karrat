/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.struct.ByteBuffer
import javax.crypto.Cipher

class EncryptionHandler(private val encryptor: Cipher, private val decryptor: Cipher) {
    
    fun cipher(buffer: ByteBuffer) {
        buffer.bytes = encryptor.update(buffer.bytes, 0, buffer.size)
    }

    fun decipher(buffer : ByteBuffer) {
        buffer.bytes = decryptor.update(buffer.bytes, 0, buffer.size)
    }
    
}