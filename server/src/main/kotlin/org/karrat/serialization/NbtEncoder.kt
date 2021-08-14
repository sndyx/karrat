/*
 * Copyright © Karrat - 2021.
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