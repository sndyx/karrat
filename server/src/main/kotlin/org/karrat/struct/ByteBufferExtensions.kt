/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.karrat.internal.NioByteBuffer
import org.karrat.play.ChatComponent

/**
 * Writes a [ByteArray] from the buffer, prefixed with its length.
 */
public fun MutableByteBuffer.writePrefixed(value: ByteArray) {
    writeVarInt(value.size)
    writeBytes(value)
}

/**
 * Writes a variable-length Int to the buffer.
 */
public fun MutableByteBuffer.writeVarInt(value: Int) {
    var i = value
    do {
        var currentByte = i and 127
        i = i ushr 7
        if (i != 0) currentByte = currentByte or 128
        write(currentByte.toByte())
    } while (i != 0)
}

/**
 * Writes a variable-length Long to the buffer.
 */
public fun MutableByteBuffer.writeVarLong(value: Long) {
    var i = value
    do {
        var currentByte = i and 127
        i = i ushr 7
        if (i != 0L) currentByte = currentByte or 128
        write(currentByte.toByte())
    } while (i != 0L)
}

public fun varSizeOf(value: Int): Int {
    if (value < 0) return 5
    if (value < 0x80) return 1
    if (value < 0x4000) return 2
    if (value < 0x200000) return 3
    return if (value < 0x10000000) 4 else 5
}

public fun varSizeOf(value: Long): Int {
    if (value < 0L) return 10
    if (value < 0x80L) return 1
    if (value < 0x4000L) return 2
    if (value < 0x200000L) return 3
    if (value < 0x10000000L) return 4
    if (value < 0x800000000L) return 5
    if (value < 0x40000000000L) return 6
    if (value < 0x2000000000000L) return 7
    return if (value < 0x100000000000000L) 8 else 9
}

/**
 * Writes a String to the buffer, prefixed with its length.
 */
public fun MutableByteBuffer.writeString(value: String) {
    val bytes = value.encodeToByteArray()
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

/**
 * Writes a [Uuid] to the buffer.
 */
public fun MutableByteBuffer.writeUuid(value: Uuid) {
    writeLong(value.mostSignificantBits)
    writeLong(value.leastSignificantBits)
}

/**
 * Writes a [ChatComponent] to the buffer.
 */
public fun MutableByteBuffer.writeChatComponent(value: ChatComponent): Unit = writeString(Json.encodeToString(value))

/**
 * Reads a [ByteArray] with its size prefixed as a variable-length Int from the buffer.
 */
public fun ByteBuffer.readPrefixed(): ByteArray {
    val length = readVarInt()
    return readBytes(length)
}

/**
 * Reads a variable-length Int from the buffer.
 */
public fun ByteBuffer.readVarInt(): Int {
    var value = 0
    var offset = 0
    var byte: Int
    do {
        check(offset != 35) { "VarInt is too big" }
        byte = read().toInt()
        value = value or (byte and 127 shl offset)
        offset += 7
    } while (byte and 128 != 0)
    return value
}

/**
 * Reads a variable-length Long from the buffer.
 */
public fun ByteBuffer.readVarLong(): Long {
    var value = 0L
    var offset = 0
    var byte: Long
    do {
        check(offset != 70) { "VarLong is too big" }
        byte = read().toLong()
        value = value or (byte and 127 shl offset)
        offset += 7
    } while (byte and 128 != 0L)
    return value
}

/**
 * Reads a String from the buffer.
 */
public fun ByteBuffer.readString(): String {
    val length = readVarInt()
    return readBytes(length).decodeToString()
}

/**
 * Reads a [Uuid] from the buffer.
 */
public fun ByteBuffer.readUuid(): Uuid = Uuid(readLong(), readLong())

/**
 * Reads a [ChatComponent] from the buffer.
 */
public fun ByteBuffer.readChatComponent(): ChatComponent = Json.decodeFromString(readString())

public fun MutableByteBuffer.writeUByte(value: UByte): Unit = write(value.toByte())

public fun MutableByteBuffer.writeUShort(value: UShort): Unit = writeShort(value.toShort())

public fun MutableByteBuffer.writeUInt(value: UInt): Unit = writeInt(value.toInt())

public fun MutableByteBuffer.writeULong(value: ULong): Unit = writeLong(value.toLong())

public fun ByteBuffer.readUByte(): UByte = read().toUByte()

public fun ByteBuffer.readUShort(): UShort = readShort().toUShort()

public fun ByteBuffer.readUInt(): UInt = readInt().toUInt()

public fun ByteBuffer.readULong(): ULong = readLong().toULong()

internal fun ByteBuffer.nio(): NioByteBuffer {
    return NioByteBuffer.allocate(size).also { it.put(array()) }
}