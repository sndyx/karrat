/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.karrat.item.NbtCompound

@OptIn(ExperimentalSerializationApi::class)
internal class NbtDecoder(private val input: NbtCompound, private val descriptor: SerialDescriptor) : AbstractDecoder() {
    
    private var index = 0
    private val key get() = descriptor.getElementName(index++)
    
    override val serializersModule = EmptySerializersModule
    
    override fun beginStructure(descriptor: SerialDescriptor) =
        NbtDecoder(input, descriptor)
    
    override fun decodeElementIndex(descriptor: SerialDescriptor) =
        if (index == descriptor.elementsCount) CompositeDecoder.DECODE_DONE
        else index
    
    override fun decodeSequentially() = true
    
    override fun decodeBoolean(): Boolean = input.get<Byte>(key) == 1.toByte()
    override fun decodeByte(): Byte = input.get(key)
    override fun decodeShort(): Short = input.get(key)
    override fun decodeInt(): Int = input.get(key)
    override fun decodeLong(): Long = input.get(key)
    override fun decodeFloat(): Float = input.get(key)
    override fun decodeDouble(): Double = input.get(key)
    override fun decodeChar(): Char = input.get<Int>(key).toChar()
    override fun decodeString(): String = input.get(key)
    
    @Suppress("UNCHECKED_CAST") // heck you kotlin!!! i live life on the edge !
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return if (deserializer.descriptor.kind == SerialKind.ENUM) {
            val value = decodeString().split(':')
            Class.forName(value[0]).enumConstants[value[1].toInt()] as T
        } else {
            val compound = input.get<NbtCompound>(key)
            val decoder = NbtDecoder(compound, deserializer.descriptor)
            deserializer.deserialize(decoder)
        }
    }
    
    fun <T> decode(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }
    
}