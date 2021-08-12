/*
 * This file is part of Karrat.
 *
 * Karrat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Karrat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Karrat.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.karrat.util

class ByteBuffer {
    
    private var bytes: ByteArray
    private var pointer = -1
    private var pos = 0
    val size get() = pointer + 1
    
    constructor() {
        bytes = ByteArray(1)
    }
    
    constructor(values: ByteArray) {
        bytes = values
        pointer += bytes.size - 1
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
    
    fun readShort() = read().toInt() and 0xff shl 8 or (read().toInt() and 0xff)
    
    fun readInt(): Int {
        var value = 0
        for (i in 3 downTo 0) value = value or (read().toInt() and 0xff shl (8 * i))
        return value
    }
    
    fun readLong(): Long {
        var value = 0L
        for (i in 7 downTo 0) value = value or (read().toLong() and 0xff shl (8 * i))
        return value
    }
    
    fun readFloat() = Float.fromBits(readInt())
    
    fun readDouble() = Double.fromBits(readLong())
    
    fun readBytes(amount: Int = size - pos): ByteArray {
        val value = bytes.copyOfRange(pos, pos + amount)
        pos += amount
        return value
    }
    
    fun readStream(amount: Int = size - pos): ByteBuffer {
        return ByteBuffer(readBytes(amount))
    }
    
    fun reset() {
        pos = 0
    }
    
    fun array(): ByteArray = bytes.copyOf(size)
    
    private val remaining: ByteArray get() = bytes
        .copyOfRange(size - (size - pos), size)
    
    override fun toString() = "ByteBuffer(bytes=[" +
            remaining.joinToString(", ") { "%02x".format(it) } + "])"
    
}