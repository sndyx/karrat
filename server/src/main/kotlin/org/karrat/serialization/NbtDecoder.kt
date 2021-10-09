/*
 * Copyright © Karrat - 2021.
 */
@file:OptIn(ExperimentalSerializationApi::class)

package org.karrat.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.karrat.struct.NbtCompound

internal fun <T> Nbt.readNbt(element: NbtCompound, deserializer: DeserializationStrategy<T>): T {
    val input = NbtDecoder(element)
    return input.decodeSerializableValue(deserializer)
}

private abstract class AbstractNbtDecoder(
    val value: Any
) : Decoder, CompositeDecoder {
    
    abstract fun currentElement(tag: String): Any
    abstract fun <T> decodeElement(tag: String): T
    
    fun currentElementOrValue(): Any {
        return if (currentTagOrNull == null) value
        else currentElement(currentTagOrNull!!)
    }
    
    override val serializersModule: SerializersModule = EmptySerializersModule
    
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            StructureKind.LIST, is PolymorphicKind -> NbtListDecoder(currentElementOrValue())
            else -> NbtDecoder(currentElementOrValue())
        }
    }
    
    override fun endStructure(descriptor: SerialDescriptor) { /* Ignore */ }
    
    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        return tagBlock(descriptor.getElementName(index)) { decodeSerializableValue(deserializer) }
    }
    
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? = null
    
    // >:)
    // block of code > block of diamond
    override fun decodeBoolean(): Boolean = decodeElement(pop())
    override fun decodeByte(): Byte = decodeElement(pop())
    override fun decodeShort(): Short= decodeElement(pop())
    override fun decodeInt(): Int = decodeElement(pop())
    override fun decodeLong(): Long = decodeElement(pop())
    override fun decodeFloat(): Float = decodeElement(pop())
    override fun decodeDouble(): Double = decodeElement(pop())
    override fun decodeChar(): Char = decodeElement<Int>(pop()).toChar()
    override fun decodeString(): String= decodeElement(pop())
    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = decodeElement(descriptor.getElementName(index))
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = decodeElement(descriptor.getElementName(index))
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = decodeElement(descriptor.getElementName(index))
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = decodeElement(descriptor.getElementName(index))
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = decodeElement(descriptor.getElementName(index))
    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = decodeElement(descriptor.getElementName(index))
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = decodeElement(descriptor.getElementName(index))
    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = decodeElement(descriptor.getElementName(index))
    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = decodeElement(descriptor.getElementName(index))
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeElement(pop())
    override fun decodeNull(): Nothing? = null // Top 10 useful functions
    override fun decodeNotNullMark(): Boolean = false
    
    override fun decodeInline(inlineDescriptor: SerialDescriptor): Decoder = this.apply { push(pop()) }
    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = this.apply {
        push(descriptor.getElementName(index))
    }
    
    private fun <T> tagBlock(tag: String, block: () -> T): T {
        push(tag)
        val r = block()
        if (!flag) {
            pop()
        }
        flag = false
        return r
    }
    
    private val tags = mutableListOf<String>()
    private val currentTag: String get() = tags.last()
    private val currentTagOrNull: String? get() = tags.lastOrNull()
    
    private var flag = false
    
    private fun push(tag: String) {
        tags.add(tag)
    }
    
    private fun pop(): String {
        val tag = tags.removeLast()
        flag = true
        return tag
    }
    
}

private class NbtDecoder(value: Any) : AbstractNbtDecoder(value) {
    
    private var position = 0
    
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (position < descriptor.elementsCount) {
            val name = descriptor.getElementName(position++)
            if (name in (value as NbtCompound)) {
                return position - 1
            }
        }
        return CompositeDecoder.DECODE_DONE
    }
    
    override fun currentElement(tag: String): Any = (value as NbtCompound).getValue(tag)
    
    @Suppress("Unchecked_Cast")
    override fun <T> decodeElement(tag: String): T {
        (value as NbtCompound).let {
            return it[tag] as T
        }
    }
    
}

private class NbtListDecoder(value: Any) : AbstractNbtDecoder(value) {
    
    private val size = (value as List<*>).size
    private var currentIndex = -1
    
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (currentIndex < size - 1) {
            return currentIndex++
        }
        return CompositeDecoder.DECODE_DONE
    }
    
    override fun currentElement(tag: String): Any = (value as List<*>)[tag.toInt()]!!
    
    @Suppress("Unchecked_Cast")
    override fun <T> decodeElement(tag: String): T {
        return (value as List<*>)[tag.toInt()] as T
    }
    
}