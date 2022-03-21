/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.serialization.command

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal class CommandDecoder(val list: ArrayDeque<String>, var elementsCount: Int = 0) : AbstractDecoder() {

    private var elementIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun decodeValue(): Any = list.removeFirst()
    override fun decodeBoolean(): Boolean = list.removeFirst().lowercase().toBooleanStrict()
    override fun decodeByte(): Byte = list.removeFirst().toByte()
    override fun decodeChar(): Char = list.removeFirst()[0]
    override fun decodeDouble(): Double = list.removeFirst().toDouble()
    override fun decodeFloat(): Float = list.removeFirst().toFloat()
    override fun decodeInt(): Int = list.removeFirst().toInt()
    override fun decodeLong(): Long = list.removeFirst().toLong()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = enumDescriptor.getElementIndex(list.removeFirst())

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        CommandDecoder(list, descriptor.elementsCount)

    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
        decodeInt().also { elementsCount = it }

}