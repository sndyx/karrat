/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.karrat.item.NbtCompound
import org.karrat.util.ByteBuffer
import org.karrat.util.DynamicByteBuffer
import org.karrat.util.array

object Nbt {
    
    /**
     * Encodes the [value] into an equivalent [NbtCompound] using the given
     * [SerializationStrategy].
     */
    fun <T> encodeToNbt(serializer: SerializationStrategy<T>, value: T): NbtCompound {
        val encoder = NbtEncoder(serializer.descriptor)
        encoder.encode(serializer, value)
        return encoder.output
    }
    
    /**
     * Decodes the [NbtCompound] into an equivalent [T] instance using the given
     * [DeserializationStrategy].
     */
    fun <T> decodeFromNbt(deserializer: DeserializationStrategy<T>, value: NbtCompound): T {
        val decoder = NbtDecoder(value, deserializer.descriptor)
        return decoder.decode(deserializer)
    }
    
    /**
     * Encodes the given [value] into an equivalent [NbtCompound] using the
     * serializer retrieved from the reified type parameter.
     */
    inline fun <reified T> encodeToNbt(value: T) = encodeToNbt(serializer(), value)
    
    /**
     * Decodes the given [value] into an equivalent [T] instance using the
     * serializer retrieved from the reified type parameter.
     */
    inline fun <reified T> decodeFromNbt(value: NbtCompound) = decodeFromNbt(serializer<T>(), value)
    
    /**
     * Serializes the given [NbtCompound] into a matching [ByteArray].
     */
    fun encodeToBytes(value: NbtCompound): ByteArray {
        val buffer = DynamicByteBuffer()
        writeNbt(buffer, value)
        return buffer.array()
    }
    
    /**
     * Deserializes the given [ByteArray] into a [NbtCompound].
     */
    fun decodeFromBytes(value: ByteArray): NbtCompound {
        val buffer = ByteBuffer(value)
        return readNbt(buffer, 10) as NbtCompound
    }
    
}