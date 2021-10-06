/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.karrat.struct.ByteBuffer
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.NbtCompound
import org.karrat.struct.array

object Nbt {
    
    /**
     * Encodes the [value] into an equivalent [NbtCompound] using the given
     * [SerializationStrategy].
     */
    fun <T> encodeToNbt(value: T, serializer: SerializationStrategy<T>): NbtCompound {
        return writeNbt(value, serializer)
    }
    
    /**
     * Decodes the [NbtCompound] into an equivalent [T] instance using the given
     * [DeserializationStrategy].
     */
    fun <T> decodeFromNbt(value: NbtCompound, deserializer: DeserializationStrategy<T>): T {
        return readNbt(value, deserializer)
    }
    
    /**
     * Encodes the given [value] into an equivalent [NbtCompound] using the
     * serializer retrieved from the reified type parameter.
     */
    inline fun <reified T> encodeToNbt(value: T) = encodeToNbt(value ,serializer())
    
    /**
     * Decodes the given [value] into an equivalent [T] instance using the
     * serializer retrieved from the reified type parameter.
     */
    inline fun <reified T> decodeFromNbt(value: NbtCompound) = decodeFromNbt(value, serializer<T>())
    
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