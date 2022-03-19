/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.serialization.nbt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.karrat.struct.ByteBuffer
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.NbtCompound
import org.karrat.struct.array

public object Nbt {

    /**
     * Encodes the [value] into an equivalent [NbtCompound] using the given
     * [SerializationStrategy].
     */
    public fun <T> encodeToNbt(
        value: T,
        serializer: SerializationStrategy<T>
    ): NbtCompound {
        return writeNbt(value, serializer)
    }

    /**
     * Decodes the [NbtCompound] into an equivalent [T] instance using the given
     * [DeserializationStrategy].
     */
    public fun <T> decodeFromNbt(
        value: NbtCompound,
        deserializer: DeserializationStrategy<T>
    ): T {
        return readNbt(value, deserializer)
    }

    /**
     * Encodes the given [value] into an equivalent [NbtCompound] using the
     * serializer retrieved from the reified type parameter.
     */
    public inline fun <reified T> encodeToNbt(value: T): NbtCompound =
        encodeToNbt(value, serializer())

    /**
     * Decodes the given [value] into an equivalent [T] instance using the
     * serializer retrieved from the reified type parameter.
     */
    public inline fun <reified T> decodeFromNbt(value: NbtCompound): T =
        decodeFromNbt(value, serializer())

    /**
     * Serializes the given [NbtCompound] into a matching [ByteArray].
     */
    public fun encodeToBytes(value: NbtCompound): ByteArray {
        val buffer = DynamicByteBuffer()
        writeNbtCompound(buffer, value)
        return buffer.array()
    }

    /**
     * Deserializes the given [ByteArray] into a [NbtCompound].
     */
    public fun decodeFromBytes(value: ByteArray): NbtCompound {
        val buffer = ByteBuffer(value)
        return readNbtCompound(buffer) as NbtCompound
    }

}