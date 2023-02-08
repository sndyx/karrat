/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.struct

public interface ByteBuffer {

    public var bytes: ByteArray
    public var pos: Int
    public val size: Int
    public val remaining: Int

    public fun read(): Byte

    public fun readBoolean(): Boolean

    public fun readShort(): Short

    public fun readInt(): Int

    public fun readLong(): Long

    public fun readFloat(): Float

    public fun readDouble(): Double
    
    public fun skip(bytes: Int)

    public fun reset()

    public operator fun iterator(): ByteIterator

}

public fun ByteBuffer(values: ByteArray): ByteBuffer = ByteBufferImpl(values)

internal open class ByteBufferImpl(override var bytes: ByteArray) : ByteBuffer {

    override var pos = 0
    override val size get() = bytes.size
    override val remaining get() = size - pos

    override fun read(): Byte {
        val value = bytes[pos]
        pos++
        return value
    }

    override fun readBoolean() = read() == 1.toByte()

    override fun readShort() = (read().toInt() and 0xff shl 8 or (read().toInt() and 0xff)).toShort()

    override fun readInt(): Int {
        var value = 0
        for (i in 3 downTo 0) value = value or (read().toInt() and 0xff shl (8 * i))
        return value
    }

    override fun readLong(): Long {
        var value = 0L
        for (i in 7 downTo 0) value = value or (read().toLong() and 0xff shl (8 * i))
        return value
    }

    override fun readFloat() = Float.fromBits(readInt())

    override fun readDouble() = Double.fromBits(readLong())
    
    override fun skip(bytes: Int) {
        pos += bytes
    }
    
    override fun reset() {
        pos = 0
    }

    override fun toString() = "ByteBuffer(bytes=[" +
            bytes.copyOfRange(size - remaining, size)
                .joinToString(", ") { "%02x".format(it) } + "])"

    override fun iterator(): ByteIterator =
        bytes.iterator()

    override fun equals(other: Any?): Boolean {
        if (other !is ByteBuffer) return false
        if (pos != other.pos) return false
        if (size != other.size) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + pos
        return result
    }

}

public fun ByteBuffer.readBytes(amount: Int = remaining): ByteArray {
    val value = bytes.copyOfRange(pos, pos + amount)
    pos += amount
    return value
}

public fun ByteBuffer.readBuffer(amount: Int): ByteBuffer {
    return ByteBuffer(readBytes(amount))
}

public fun ByteBuffer.array(): ByteArray = bytes.copyOf(size)

public fun ByteBuffer.contains(element: Byte): Boolean = bytes.contains(element)

public fun ByteBuffer.isEmpty(): Boolean = size == 0

public fun ByteBuffer.toByteBuffer(): ByteBuffer = ByteBufferImpl(bytes)

public fun ByteBuffer.toMutableByteBuffer(): MutableByteBuffer =
    MutableByteBufferImpl(bytes.size).apply {
        writeBytes(bytes)
    }

public fun ByteBuffer.toDynamicByteBuffer(): DynamicByteBuffer =
    DynamicByteBuffer(bytes)

public fun byteBufferOf(vararg elements: Byte): ByteBuffer = ByteBuffer(elements)

public fun ByteArray.toByteBuffer(): ByteBuffer = ByteBuffer(this)

public fun Collection<Byte>.toByteBuffer(): ByteBuffer = toByteArray().toByteBuffer()

public interface MutableByteBuffer : ByteBuffer {

    public var pointer: Int

    public fun write(value: Byte)

    public fun writeBoolean(value: Boolean)

    public fun writeShort(value: Short)

    public fun writeInt(value: Int)

    public fun writeLong(value: Long)

    public fun writeFloat(value: Float)

    public fun writeDouble(value: Double)

}

public fun MutableByteBuffer(allocation: Int): MutableByteBuffer = MutableByteBufferImpl(allocation)

internal class MutableByteBufferImpl(allocation: Int) : ByteBufferImpl(ByteArray(allocation)), MutableByteBuffer {

    override var pointer = -1
    override val size get() = pointer + 1

    override fun write(value: Byte) {
        pointer++
        check(bytes.size != pointer) { "Buffer overflow." }
        bytes[pointer] = value
    }

    override fun writeBoolean(value: Boolean) = if (value) write(1) else write(0)

    override fun writeShort(value: Short) {
        for (i in 1 downTo 0) write((value.toInt() shr 8 * i).toByte())
    }

    override fun writeInt(value: Int) {
        for (i in 3 downTo 0) write((value shr 8 * i).toByte())
    }

    override fun writeLong(value: Long) {
        for (i in 7 downTo 0) write((value shr 8 * i).toByte())
    }

    override fun writeFloat(value: Float) = writeInt(value.toBits())

    override fun writeDouble(value: Double) = writeLong(value.toBits())

}

public fun MutableByteBuffer.writeBytes(value: ByteArray) {
    value.forEach { write(it) }
}

public class DynamicByteBuffer(values: ByteArray) : ByteBuffer by ByteBufferImpl(values), MutableByteBuffer {

    override var pointer: Int = -1
    override val size: Int get() = pointer + 1

    public constructor() : this(ByteArray(16))

    override fun toString(): String = "ByteBuffer(bytes=[" +
            bytes.copyOf(size)
                .joinToString(", ") { "%02x".format(it) } + "])"

    override fun write(value: Byte) {
        pointer++
        if (bytes.size == pointer) {
            bytes = bytes.copyOf((bytes.size * 2).coerceIn(bytes.size + 1, Integer.MAX_VALUE - 8))
        }
        bytes[pointer] = value
    }

    override fun writeBoolean(value: Boolean): Unit =
        if (value) write(1) else write(0)

    override fun writeShort(value: Short) {
        for (i in 1 downTo 0) write((value.toInt() shr 8 * i).toByte())
    }

    override fun writeInt(value: Int) {
        for (i in 3 downTo 0) write((value shr 8 * i).toByte())
    }

    override fun writeLong(value: Long) {
        for (i in 7 downTo 0) write((value shr 8 * i).toByte())
    }

    override fun writeFloat(value: Float): Unit = writeInt(value.toBits())

    override fun writeDouble(value: Double): Unit = writeLong(value.toBits())

    init {
        bytes = values
    }

}