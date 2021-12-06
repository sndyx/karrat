/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable
import org.karrat.server.fatal

@JvmInline
@Serializable
public value class Identifier internal constructor(private val value: String) {
    
    public val namespace: String
        get() = value.split(':')[0]
    
    public val name: String
        get() = value.split(':')[1]
    
    override fun toString(): String = value
    
}

public fun id(values: Pair<String, String>): Identifier {
    check(!values.first.contains(':')) {
        fatal("':' character not allowed in namespace.") }
    check(!values.second.contains(':')) {
        fatal("':' character not allowed in id.") }
    return Identifier("${values.second}:${values.first}")
}

public fun id(namespace: String, id: String): Identifier {
    check(!namespace.contains(':')) {
        fatal("':' character not allowed in namespace.") }
    check(!id.contains(':')) {
        fatal("':' character not allowed in id.") }
    return Identifier("$namespace:$id")
}

public fun id(identifier: String): Identifier =
    Identifier(
        identifier.takeIf { it.matches(Regex("[^:]+:.[^:]+")) }
            ?: identifier.takeUnless { it.contains(":") }
                ?.let { "minecraft:$it" }
            ?: (fatal("':' character not allowed in id."))
    )