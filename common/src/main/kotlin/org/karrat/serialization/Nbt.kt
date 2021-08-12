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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.karrat.item.NbtCompound
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
    
}