/*
 * Copyright © Karrat - 2021.
 */
@file:Suppress("Unused")

package org.karrat.network.translation

import org.karrat.Config
import org.karrat.network.Session
import org.karrat.server.fatal
import org.karrat.struct.*
import java.util.zip.Deflater
import java.util.zip.Inflater

private val inflater by lazy { Inflater() }
private val deflater by lazy { Deflater() }

internal fun Session.decompress(buffer : ByteBuffer) {
    val uncompressedLength = buffer.readVarInt()
    if (uncompressedLength == 0) return
    if (uncompressedLength < Config.compressionThreshold) {
        fatal("Badly compressed packet - size of $uncompressedLength is below" +
                " server threshold of ${Config.compressionThreshold}")
    } else if (uncompressedLength > 8388608) {
        fatal("Badly compressed packet - size of $uncompressedLength is" +
                " larger than protocol maximum of 8388608")
    }
    
    val compressed = buffer.readBytes()
    inflater.setInput(compressed)
    val result = ByteArray(uncompressedLength)
    inflater.inflate(result)
    inflater.reset()
    buffer.bytes = result
    buffer.reset()
}

internal fun Session.compress(buffer : ByteBuffer) {
    val length = buffer.size

    val result = DynamicByteBuffer()
    if (length < Config.compressionThreshold) {
        result.writeVarInt(0)
        result.writeBytes(buffer.bytes)
    } else {
        result.writeVarInt(length)
        val uncompressed = buffer.readBytes(length)
        deflater.setInput(uncompressed)
        deflater.finish()
    
        val bytes = ByteArray(8192)
    
        deflater.deflate(bytes)
        result.writeBytes(bytes)
        deflater.reset()
    }

    buffer.bytes = result.array()
    buffer.reset()
}