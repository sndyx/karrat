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

package org.karrat.serialization

import org.karrat.item.NbtCompound
import org.karrat.util.ByteBuffer
import org.karrat.util.readUShort
import org.karrat.util.writeUShort

internal fun writeNbt(buffer: ByteBuffer, value: Any) {
    buffer.apply {
        when (value) {
            is Byte -> write(value)
            is Short -> writeShort(value)
            is Int -> writeInt(value)
            is Long -> writeLong(value)
            is Float -> writeFloat(value)
            is Double -> writeDouble(value)
            is ByteArray -> {
                writeInt(value.size)
                writeBytes(value)
            }
            is String -> {
                val bytes = value.encodeToByteArray()
                writeUShort(bytes.size.toUShort())
                writeBytes(bytes)
            }
            is List<*> -> {
                value.firstOrNull()?.let { first ->
                    val type = typeOf(first)
                    write(type)
                    writeInt(value.size)
                    value.filterNotNull().forEach {
                        writeNbt(buffer, it)
                    }
                }
            }
            is NbtCompound -> {
                value.elements.forEach {
                    write(typeOf(it.value))
                    writeNbt(buffer, it.key)
                    writeNbt(buffer, it.value)
                }
                write(0)
            }
            is IntArray -> {
                writeInt(value.size)
                value.forEach {
                    writeInt(it)
                }
            }
            is LongArray -> {
                writeInt(value.size)
                value.forEach {
                    writeLong(it)
                }
            }
            else -> error("Illegal NBT type: ${value::class.simpleName}.")
        }
    }
}

internal fun readNbt(buffer: ByteBuffer, type: Int): Any {
    buffer.apply {
        return when (type) {
            1 -> read()
            2 -> readShort()
            3 -> readInt()
            4 -> readLong()
            5 -> readFloat()
            6 -> readDouble()
            7 -> {
                val size = readInt()
                readBytes(size)
            }
            8 -> {
                val length = readUShort()
                val bytes = readBytes(length.toInt())
                bytes.decodeToString()
            }
            9 -> {
                val innerType = read().toInt()
                val size = readInt()
                val list = mutableListOf<Any>()
                for (i in 0 until size) {
                    list += readNbt(buffer, innerType)
                }
                list
            }
            10 -> {
                val map = mutableMapOf<String, Any>()
                var innerType = read().toInt()
                while (innerType != 0) {
                    val name = readNbt(buffer, 8) as String
                    map[name] = readNbt(buffer, innerType)
                    innerType = read().toInt()
                }
                NbtCompound(map)
            }
            11 -> {
                val size = readInt()
                val data = mutableListOf<Int>()
                for (i in 0 until size) {
                    data += readInt()
                }
                data.toIntArray()
            }
            12 -> {
                val size = readInt()
                val data = mutableListOf<Long>()
                for (i in 0 until size) {
                    data += readLong()
                }
                data.toLongArray()
            }
            else -> error("Illegal NBT type: $type.")
        }
    }
}

private fun typeOf(value: Any): Byte = when (value) {
    is Byte -> 1
    is Short -> 2
    is Int -> 3
    is Long -> 4
    is Float -> 5
    is Double -> 6
    is ByteArray -> 7
    is String -> 8
    is List<*> -> 9
    is NbtCompound -> 10
    is IntArray -> 11
    is LongArray -> 12
    else -> error("Illegal NBT type: ${value::class.simpleName}.")
}