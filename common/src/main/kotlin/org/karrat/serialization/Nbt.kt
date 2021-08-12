package org.karrat.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.karrat.item.NbtCompound
import org.karrat.util.readUShort
import org.karrat.util.writeUShort
import org.karrat.util.ByteBuffer

object Nbt {
    
    /**
     * Serializes the [value] into an equivalent [NbtCompound] using the given
     * [serializer].
     */
    fun <T> encodeToNbt(serializer: SerializationStrategy<T>, value: T): NbtCompound {
        val encoder = NbtEncoder(serializer.descriptor)
        encoder.encode(serializer, value)
        return encoder.output
    }
    
    fun <T> decodeFromNbt(deserializer: DeserializationStrategy<T>, value: NbtCompound): T {
        val decoder = NbtDecoder(value, deserializer.descriptor)
        return decoder.decode(deserializer)
    }
    
    /**
     * Serializes and encodes the given [value] into an equivalent [NbtCompound]
     * using the serializer retrieved from the reified type parameter.
     */
    inline fun <reified T> encodeToNbt(value: T) = encodeToNbt(serializer(), value)
    
    inline fun <reified T> decodeFromNbt(value: NbtCompound) = decodeFromNbt(serializer<T>(), value)
    
    fun encodeToBytes(value: NbtCompound): ByteArray {
        val buffer = ByteBuffer()
        writeNbt(buffer, value)
        return buffer.array()
    }
    
    fun decodeFromBytes(value: ByteArray): NbtCompound {
        val buffer = ByteBuffer(value)
        return readNbt(buffer, 10) as NbtCompound
    }
    
    private fun writeNbt(buffer: ByteBuffer, value: Any) {
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
    
    private fun readNbt(buffer: ByteBuffer, type: Int): Any {
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
    
}