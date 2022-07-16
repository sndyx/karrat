/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.nio.charset.CoderResult

private object Utf8Mod : Charset("mutf-8", emptyArray()) {

    override fun contains(p0: Charset?): Boolean = false

    override fun newDecoder(): CharsetDecoder = Utf8ModDecoder

    override fun newEncoder(): CharsetEncoder = Utf8ModEncoder

}

private object Utf8ModDecoder : CharsetDecoder(Utf8Mod, 0.4f, 1f) {

    override fun decodeLoop(data: ByteBuffer, out: CharBuffer): CoderResult {
        val input = ByteArrayInputStream(data.array())
        val result = DataInputStream(input).use { it.readUTF() }
        out.put(result)
        return CoderResult.UNDERFLOW
    }

}

private object Utf8ModEncoder : CharsetEncoder(Utf8Mod, 0.4f, 1f) {

    override fun encodeLoop(data: CharBuffer, out: ByteBuffer): CoderResult {
        val os = ByteArrayOutputStream()
        DataOutputStream(os).use { it.writeUTF(data.toString()) }
        out.put(os.toByteArray())
        return CoderResult.UNDERFLOW
    }

}

/**
 * Modified eight-bit UCS Transformation Format.
 *
 * @see java.io.DataInput
 */
public val Charsets.MUTF_8: Charset
    get() = Utf8Mod