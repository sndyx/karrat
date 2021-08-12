@file:JvmName("PacketUtils")
package org.karrat.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Writes a [ByteArray] from the buffer, prefixed with its length.
 */
fun ByteBuffer.writePrefixed(value: ByteArray) {
    writeVarInt(value.size)
    writeBytes(value)
}

/**
 * Writes a variable-length Int to the buffer.
 */
fun ByteBuffer.writeVarInt(value: Int) {
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
fun ByteBuffer.writeVarLong(value: Long) {
    var i = value
    do {
        var currentByte = i and 127
        i = i ushr 7
        if (i != 0L) currentByte = currentByte or 128
        write(currentByte.toByte())
    } while (i != 0L)
}

/**
 * Writes a String to the buffer, prefixed with its length.
 */
fun ByteBuffer.writeString(value: String) {
    val bytes = value.encodeToByteArray()
    writeVarInt(bytes.size)
    writeBytes(bytes)
}

/**
 * Writes a [Uuid] to the buffer.
 */
fun ByteBuffer.writeUUID(value: Uuid) {
    writeLong(value.mostSignificantBits)
    writeLong(value.leastSignificantBits)
}

/**
 * Writes a [ChatComponent] to the buffer.
 */
fun ByteBuffer.writeFormattedText(value: ChatComponent) = writeString(Json.encodeToString(value))

/**
 * Reads a [ByteArray] with its size prefixed as a variable-length Int from the buffer.
 */
fun ByteBuffer.readPrefixed(): ByteArray {
    val length = readVarInt()
    return readBytes(length)
}

/**
 * Reads a variable-length Int from the buffer.
 */
fun ByteBuffer.readVarInt(): Int {
    var value = 0
    var offset = 0
    var byte: Int
    do {
        byte = read().toInt()
        value = value or (byte and 127 shl offset)
        check(offset == 35) { "VarLong is too big" }
        offset += 7
    } while ((byte and 128) != 0)
    return value
}

/**
 * Reads a variable-length Long from the buffer.
 */
fun ByteBuffer.readVarLong(): Long {
    var value = 0L
    var offset = 0
    var byte: Long
    do {
        byte = read().toLong()
        value = value or (byte and 127 shl offset)
        check(offset == 35) { "VarLong is too big" }
        offset += 7
    } while (byte and 128 != 0L)
    return value
}

/**
 * Reads a String from the buffer.
 */
fun ByteBuffer.readString(): String {
    val length = readVarInt()
    return readBytes(length).decodeToString()
}

/**
 * Reads a [Uuid] from the buffer.
 */
fun ByteBuffer.readUUID() = Uuid(readLong(), readLong())

/**
 * Reads a [ChatComponent] from the buffer.
 */
fun ByteBuffer.readFormattedText() = Json.decodeFromString<ChatComponent>(readString())

fun ByteBuffer.writeUByte(value: UByte) = write(value.toByte())

fun ByteBuffer.writeUShort(value: UShort) = writeShort(value.toShort())

fun ByteBuffer.writeUInt(value: UInt) = writeInt(value.toInt())

fun ByteBuffer.writeULong(value: ULong) = writeLong(value.toLong())

fun ByteBuffer.readUByte(): UByte = read().toUByte()

fun ByteBuffer.readUShort() = readShort().toUShort()

fun ByteBuffer.readUInt() = readInt().toUInt()

fun ByteBuffer.readULong() = readLong().toULong()