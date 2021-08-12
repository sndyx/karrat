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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.karrat.item.NbtCompound

@OptIn(ExperimentalSerializationApi::class)
internal class NbtEncoder(private val descriptor: SerialDescriptor) : AbstractEncoder() {
    
    val output = NbtCompound()
    private var index = 0
    private val key get() = descriptor.getElementName(index++)
    
    override val serializersModule = EmptySerializersModule
    
    override fun encodeBoolean(value: Boolean) = output.put(key, if (value) 1.toByte() else 0.toByte())
    override fun encodeByte(value: Byte) = output.put(key, value)
    override fun encodeShort(value: Short) = output.put(key, value)
    override fun encodeInt(value: Int) = output.put(key, value)
    override fun encodeLong(value: Long) = output.put(key, value)
    override fun encodeFloat(value: Float) = output.put(key, value)
    override fun encodeDouble(value: Double) = output.put(key, value)
    override fun encodeChar(value: Char) = output.put(key, value.code)
    override fun encodeString(value: String) = output.put(key, value)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = output.put(key, index)
    
    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        if (serializer.descriptor.kind == SerialKind.ENUM) {
            encodeString("${value!!::class.java.canonicalName}:${serializer.descriptor.getElementIndex(value.toString())}")
        } else {
            val encoder = NbtEncoder(serializer.descriptor)
            serializer.serialize(encoder, value)
            output.put(key, encoder.output)
        }
    }
    
    fun <T> encode(serializer: SerializationStrategy<T>, value: T) {
        serializer.serialize(this, value)
    }
    
}