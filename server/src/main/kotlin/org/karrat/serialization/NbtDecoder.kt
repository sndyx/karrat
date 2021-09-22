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
    
    override fun decodeBoolean(): Boolean = input[key] == 1.toByte()
    override fun decodeByte(): Byte = input[key] as Byte
    override fun decodeShort(): Short = input[key] as Short
    override fun decodeInt(): Int = input[key] as Int
    override fun decodeLong(): Long = input[key] as Long
    override fun decodeFloat(): Float = input[key] as Float
    override fun decodeDouble(): Double = input[key] as Double
    override fun decodeChar(): Char = (input[key] as Int).toChar()
    override fun decodeString(): String = input[key] as String
    
    @Suppress("UNCHECKED_CAST") // heck you kotlin!!! i live life on the edge !
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return if (deserializer.descriptor.kind == SerialKind.ENUM) {
            val value = decodeString().split(':')
            Class.forName(value[0]).enumConstants[value[1].toInt()] as T
        } else {
            val compound = input[key] as NbtCompound
            val decoder = NbtDecoder(compound, deserializer.descriptor)
            deserializer.deserialize(decoder)
        }
    }
    
    fun <T> decode(deserializer: DeserializationStrategy<T>): T {
        return deserializer.deserialize(this)
    }
    
}