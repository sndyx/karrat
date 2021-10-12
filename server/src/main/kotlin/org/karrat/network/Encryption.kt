/*
 * Copyright © Karrat - 2021.
 */
@file:Suppress("Unused")

package org.karrat.network

import org.karrat.Server
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException

internal fun Session.cipher(buffer: ByteBuffer) {
    buffer.bytes = ciphers.first.update(buffer.bytes, 0, buffer.size)
}

internal fun Session.decipher(buffer: ByteBuffer) {
    buffer.bytes = ciphers.second.update(buffer.bytes, 0, buffer.size)
}

@Suppress("Unused")
internal fun Server.generateKeyPair(): KeyPair {
    return try {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPairGen.initialize(1024)
        keyPairGen.generateKeyPair()
    } catch (e : NoSuchAlgorithmException) {
        fatal("Key pair generation failed!")
    }
}