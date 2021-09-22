/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.util

interface ByteBuffer {
    
    var bytes: ByteArray
    var pos: Int
    val size: Int
    val remaining: Int
    
    fun read(): Byte
    
    fun readBoolean(): Boolean
    
    fun readShort(): Short
    
    fun readInt(): Int
    
    fun readLong(): Long
    
    fun readFloat(): Float
    
    fun readDouble(): Double
    
    fun reset()
    
    operator fun iterator(): ByteIterator
    
}

fun ByteBuffer(values: ByteArray): ByteBuffer = ByteBufferImpl(values)

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
    
    override fun reset() {
        pos = 0
    }
    
    override fun toString() = "ByteBuffer(bytes=[" +
            bytes.copyOfRange(size - remaining, size).joinToString(", ") { "%02x".format(it) } + "])"
    
    override fun iterator(): ByteIterator =
        bytes.iterator()
    
}

fun ByteBuffer.readBytes(amount: Int = size - remaining): ByteArray {
    val value = bytes.copyOfRange(pos, pos + amount)
    pos += amount
    return value
}

fun ByteBuffer.readBuffer(amount: Int): ByteBuffer {
    return ByteBuffer(readBytes(amount))
}

fun ByteBuffer.array(): ByteArray = bytes.copyOf(size)

fun ByteBuffer.contains(element: Byte): Boolean =
    bytes.contains(element)

fun ByteBuffer.isEmpty(): Boolean =
    size == 0

fun ByteBuffer.toByteBuffer(): ByteBuffer = ByteBufferImpl(bytes)

fun ByteBuffer.toMutableByteBuffer(): MutableByteBuffer = MutableByteBufferImpl(bytes.size).apply {
    writeBytes(bytes)
}

fun ByteBuffer.toDynamicByteBuffer() = DynamicByteBuffer(bytes)

fun byteBufferOf(vararg elements: Byte): ByteBuffer = ByteBuffer(elements)

fun ByteArray.toByteBuffer() = ByteBuffer(this)

fun Collection<Byte>.toByteBuffer() = toByteArray().toByteBuffer()

interface MutableByteBuffer : ByteBuffer {
    
    var pointer: Int
    
    fun write(value: Byte)
    
    fun writeBoolean(value: Boolean)
    
    fun writeShort(value: Short)
    
    fun writeInt(value: Int)
    
    fun writeLong(value: Long)
    
    fun writeFloat(value: Float)
    
    fun writeDouble(value: Double)
    
}

fun MutableByteBuffer(allocation: Int): MutableByteBuffer = MutableByteBufferImpl(allocation)

internal open class MutableByteBufferImpl(allocation: Int) : ByteBufferImpl(ByteArray(allocation)), MutableByteBuffer {
    
    override var pointer = -1
    
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

fun MutableByteBuffer.writeBytes(value: ByteArray) {
    value.forEach { write(it) }
}

class DynamicByteBuffer(values: ByteArray) : MutableByteBuffer by MutableByteBufferImpl(values.size) {
    
    constructor() : this(ByteArray(0))
    
    override fun write(value: Byte) {
        pointer++
        if (bytes.size == pointer) {
            bytes = bytes.copyOf(size + (size shl 1))
        }
        bytes[pointer] = value
    }
    
    init {
        writeBytes(values)
        bytes.isEmpty().then {
            bytes = ByteArray(1)
        }
    }
    
}