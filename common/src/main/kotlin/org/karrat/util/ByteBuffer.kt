package org.karrat.util

class ByteBuffer {
    
    private var bytes: ByteArray
    private var pointer = -1
    private var pos = 0
    val size get() = pointer + 1
    val remaining get() = size - pos
    
    constructor() {
        bytes = ByteArray(1)
    }
    
    constructor(values: ByteArray) {
        bytes = values
    }
    
    fun write(value: Byte) {
        pointer++
        if (bytes.size == pointer) {
            bytes = bytes.copyOf(size + (size shl 1))
        }
        bytes[pointer] = value
    }
    
    fun writeBoolean(value: Boolean) = if (value) write(1) else write(0)
    
    fun writeShort(value: Short) {
        for (i in 1 downTo 0) write((value.toInt() shr 8 * i).toByte())
    }
    
    fun writeInt(value: Int) {
        for (i in 3 downTo 0) write((value shr 8 * i).toByte())
    }
    
    fun writeLong(value: Long) {
        for (i in 7 downTo 0) write((value shr 8 * i).toByte())
    }
    
    fun writeFloat(value: Float) = writeInt(value.toBits())
    
    fun writeDouble(value: Double) = writeLong(value.toBits())
    
    fun writeBytes(value: ByteArray) {
        value.forEach { write(it) }
    }
    
    fun read(): Byte {
        val value = bytes[pos]
        pos++
        return value
    }
    
    fun readBoolean() = read() == 1.toByte()
    
    fun readShort() = (read().toInt() or (read().toInt() shl 8)).toShort()
    
    fun readInt(): Int {
        var value = 0
        for (i in 0..3) value = value or (read().toInt() shl 8 * i)
        return value
    }
    
    fun readLong(): Long {
        var value = 0L
        for (i in 0..7) value = value or (read().toLong() shl 8 * i)
        return value
    }
    
    fun readFloat(): Float {
        var value = 0
        for (i in 0..3) value = value or (read().toInt() shl 8 * i)
        return Float.fromBits(value)
    }
    
    fun readDouble(): Double {
        var value = 0L
        for (i in 0..7) value = value or (read().toLong() shl 8 * i)
        return Double.fromBits(value)
    }
    
    fun readBytes(amount: Int = remaining): ByteArray {
        val value = bytes.copyOfRange(pos, pos + amount)
        pos += amount
        return value
    }
    
    fun readStream(amount: Int = remaining): ByteBuffer {
        return ByteBuffer(readBytes(amount))
    }
    
    fun reset() {
        pos = 0
    }
    
    fun array() = bytes.copyOf(size)
    
}