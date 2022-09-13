/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.StructureKind

public fun Command.syntax(): String {
    val builder = StringBuilder()
    when (this) {
        is CommandNodeLiteral -> {
            builder.append(literals[0])
        }
        is CommandNodeArgument<*> -> {
            builder.append(formatted())
        }
        is CommandNodeRedirect -> {
            builder.append("-> ").append(when (redirectNode) {
                is CommandNodeLiteral -> {
                    redirectNode.literals[0]
                }
                is CommandNodeArgument<*> -> {
                    builder.append(redirectNode.formatted())
                }
                else -> "null"
            })
            return builder.toString()
        }
    }
    builder.append(' ')
    when (nodes.size) {
        0 -> return builder.toString()
        1 -> builder.append(nodes[0].syntax())
        else -> builder.append('[').append(nodes.joinToString(" | ") { it.syntax() }).append(']')
    }
    return builder.toString()
}

@OptIn(ExperimentalSerializationApi::class)
private fun CommandNodeArgument<*>.formatted(): String {
    val type = if (serializer.descriptor.kind is StructureKind.LIST)
        serializer.descriptor.getElementDescriptor(0)
            .serialName
            .split('.')
            .last()
            .lowercase()
            .let { "$it..." }
    else serializer.descriptor.serialName
        .split('.')
        .last()
        .lowercase()
    return label?.let { "<$it: $type>" } ?: "<$type>"
}