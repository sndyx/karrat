/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

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
        while (data.remaining() >= 2) {
            val c = data.char.code
            if (c >= 127) break
            out.put(c.toChar())
        }
        var c2 = 0
        var c3 = 0
        while (data.remaining() >= 2) {
            val c = data.char.code and 0xff
            when (c shr 4) {
                0, 1, 2, 3, 4, 5, 6, 7 -> out.put(c.toChar())
                12, 13 -> out.put((c and 0x1f shl 6 or c2 and 0x3f).toChar())
                14 -> out.put((c and 0x0F shl 12 or (c2 and 0x3F shl 6) or (c3 and 0x3F shl 0)).toChar())
                else -> CoderResult.malformedForLength(data.position())
            }
            c2 = c
            c3 = c2
        }
        return CoderResult.UNDERFLOW
    }

}

private object Utf8ModEncoder : CharsetEncoder(Utf8Mod, 1.1f, 3.0f) {

    override fun encodeLoop(data: CharBuffer, out: ByteBuffer): CoderResult {
        while (data.hasRemaining()) {
            val c = data.get().code
            if (c >= 0x80 || c == 0) break
            out.put(c.toByte())
        }
        while (data.hasRemaining()) {
            val c = data.get().code
            if (c < 0x80 && c != 0) {
                out.put(c.toByte())
            } else if (c >= 0x800) {
                out.put(((0xE0 or (c shr 12 and 0x0F)).toByte()))
                out.put(((0x80 or (c shr 6 and 0x3F)).toByte()))
                out.put(((0x80 or (c shr 0 and 0x3F)).toByte()))
            } else {
                out.put(((0xC0 or (c shr 6 and 0x1F)).toByte()))
                out.put(((0x80 or (c shr 0 and 0x3F)).toByte()))
            }
        }
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