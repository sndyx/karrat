/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.serialization

import org.karrat.struct.*

/**
 * Writes a nameless NbtCompound to a buffer
 */
internal fun writeNBTCompound(buffer: MutableByteBuffer, value: NbtCompound) {
    writeNbt(buffer, value)
}

/**
 * Writes a nameless NBTList to a buffer
 */
internal fun writeNBTList(buffer: MutableByteBuffer, value: List<*>) {
    writeNbt(buffer, value)
}

private fun writeNbt(buffer: MutableByteBuffer, value: Any, needsId : Boolean = true) {
    buffer.apply {
        when (value) {
            is Byte -> {
                if (needsId) write(1)
                write(value)
            }
            is Short -> {
                if (needsId) write(2)
                writeShort(value)
            }
            is Int -> {
                if (needsId) write(3)
                writeInt(value)
            }
            is Long -> {
                if (needsId) write(4)
                writeLong(value)
            }
            is Float -> {
                if (needsId) write(5)
                writeFloat(value)
            }
            is Double -> {
                if (needsId) write(6)
                writeDouble(value)
            }
            is ByteArray -> {
                if (needsId) write(7)
                writeInt(value.size)
                writeBytes(value)
            }
            is String -> {
                if (needsId) write(8)
                val bytes = value.encodeToByteArray()
                writeUShort(bytes.size.toUShort())
                writeBytes(bytes)
            }
            is List<*> -> {
                if (needsId) write(9)
                value.firstOrNull()?.let { first ->
                    val type = typeOf(first)
                    write(type)

                    val list = listOfNotNull(value)

                    writeInt(list.size)
                    list.forEach {
                        writeNbt(buffer, it, needsId = false)
                    }
                }
            }
            is NbtCompound -> {
                if (needsId) write(10)
                value.entries.forEach {
                    write(typeOf(value))
                    writeNbt(buffer, it.key, needsId = false)
                    writeNbt(buffer, it.value, needsId = false)
                }
                write(0)
            }
            is IntArray -> {
                if (needsId) write(11)
                writeInt(value.size)
                value.forEach {
                    writeInt(it)
                }
            }
            is LongArray -> {
                if (needsId) write(12)
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
internal fun readNbtValue(buffer: ByteBuffer): Any {
    return readNbt(buffer, buffer.read().toInt())
}

private fun readNbt(buffer: ByteBuffer, type: Int = -1): Any {
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