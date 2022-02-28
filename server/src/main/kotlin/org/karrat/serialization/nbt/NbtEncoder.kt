/*
 * Copyright © Karrat - 2021.
 */
@file:OptIn(ExperimentalSerializationApi::class)

package org.karrat.serialization.nbt

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.karrat.struct.NbtCompound

internal fun <T> writeNbt(value: T, serializer: SerializationStrategy<T>): NbtCompound {
    lateinit var result: Any
    val encoder = NbtEncoder { result = it }
    encoder.encodeSerializableValue(serializer, value)
    return result as NbtCompound
}

private abstract class AbstractNbtEncoder(
    private val consumer: (Any) -> Unit
) : Encoder, CompositeEncoder {

    override val serializersModule = EmptySerializersModule

    abstract fun encodeElement(key: String, value: Any)
    abstract fun value(): Any

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val consumer =
            if (currentTagOrNull == null) consumer
            else { node -> encodeElement(currentTag, node) }
        val encoder = when (descriptor.kind) {
            StructureKind.LIST, is PolymorphicKind -> NbtListEncoder(consumer)
            else -> NbtEncoder(consumer)
        }
        return encoder
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (tags.isNotEmpty()) {
            pop()
        }
        consumer(value())
    }

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        check(currentTagOrNull != null || (serializer.descriptor.kind !is PrimitiveKind && serializer.descriptor.kind !== SerialKind.ENUM)) {
            "Non-structured data type on top-level in Nbt: ${value!!::class.simpleName}." // bruh why u tryna serialize a numba my man ???
        }
        serializer.serialize(this, value)
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        push(descriptor.getElementName(index))
        encodeSerializableValue(serializer, value)
    }

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        push(descriptor.getElementName(index))
        encodeNullableSerializableValue(serializer, value)
    }

    // >:)
    // block of code > block of diamond
    override fun encodeBoolean(value: Boolean) {
        encodeElement(pop(), value)
    }

    override fun encodeByte(value: Byte) {
        encodeElement(pop(), value)
    }

    override fun encodeShort(value: Short) {
        encodeElement(pop(), value)
    }

    override fun encodeInt(value: Int) {
        encodeElement(pop(), value)
    }

    override fun encodeLong(value: Long) {
        encodeElement(pop(), value)
    }

    override fun encodeFloat(value: Float) {
        encodeElement(pop(), value)
    }

    override fun encodeDouble(value: Double) {
        encodeElement(pop(), value)
    }

    override fun encodeChar(value: Char) {
        encodeElement(pop(), value.code)
    }

    override fun encodeString(value: String) {
        encodeElement(pop(), value)
    }

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        encodeElement(descriptor.getElementName(index), value.code)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        encodeElement(descriptor.getElementName(index), value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encodeElement(pop(), index)
    }

    override fun encodeNull() { /* Ignore */
    }

    override fun encodeInline(inlineDescriptor: SerialDescriptor): Encoder = this.apply { push(pop()) }
    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder = this.apply {
        push(descriptor.getElementName(index))
    }

    private val tags = mutableListOf<String>()
    private val currentTag: String get() = tags.last()
    private val currentTagOrNull: String? get() = tags.lastOrNull()

    private fun push(tag: String) {
        tags.add(tag)
    }

    private fun pop(): String =
        tags.removeLast()

}

private class NbtEncoder(
    consumer: (Any) -> Unit
) : AbstractNbtEncoder(consumer) {

    private val content = NbtCompound()

    override fun encodeElement(key: String, value: Any) {
        content[key] = value
    }

    override fun value(): Any = content

}

private class NbtListEncoder(
    consumer: (Any) -> Unit
) : AbstractNbtEncoder(consumer) {

    private val content: ArrayList<Any> = arrayListOf()

    override fun encodeElement(key: String, value: Any) {
        content.add(key.toInt(), value)
    }

    override fun value(): Any = content

}