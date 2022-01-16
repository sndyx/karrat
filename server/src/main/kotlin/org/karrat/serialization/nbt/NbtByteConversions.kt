/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.serialization.nbt

import org.karrat.server.fatal
import org.karrat.struct.*

/**
 * Writes a nameless NbtCompound to a buffer
 */
internal fun writeNbtCompound(buffer: MutableByteBuffer, value: NbtCompound) {
    writeNbt(buffer, value)
}

/**
 * Writes a nameless NBTList to a buffer
 */
internal fun writeNbtList(buffer: MutableByteBuffer, value: List<*>) {
    writeNbt(buffer, value)
}

internal fun writeNbt(buffer: MutableByteBuffer, value: Any) {
    buffer.apply {
        when (value) {
            is Byte -> {
                write(value)
            }
            is Short -> {
                writeShort(value)
            }
            is Int -> {
                writeInt(value)
            }
            is Long -> {
                writeLong(value)
            }
            is Float -> {
                writeFloat(value)
            }
            is Double -> {
                writeDouble(value)
            }
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
                val filteredVal = value.filterNotNull()

                if (filteredVal.isNotEmpty()) {
                    filteredVal.first().let { first ->
                        val type = typeOf(first)
                        write(type)
                    }

                    writeInt(filteredVal.size)
                    filteredVal.forEach {
                        writeNbt(buffer, it)
                    }
                }
            }
            is NbtCompound -> {
                value.entries.forEach {
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
            else -> error { "Illegal NBT type: ${value::class.simpleName}." }
        }
    }
}

/**
 * Reads a nameless Nbt Compound value
 */
internal fun readNbtCompound(buffer: ByteBuffer): Any {
    return readNbt(buffer, 10)
}

internal fun readNbt(buffer: ByteBuffer, type: Int = -1): Any {
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
                list.toList()
            }
            10 -> {
                val map = mutableMapOf<String, Any>()
                var innerType = read().toInt()
                while (innerType != 0) {
                    val name = readNbt(buffer, 8) as String
                    map[name] = readNbt(buffer, innerType)
                    innerType = read().toInt()
                }
                map.toNbtCompound()
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
            else -> error { "Illegal NBT type: $type." }
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
    else -> error { "Illegal NBT type: ${value::class.simpleName}." }
}