/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable

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
    require(!values.first.contains(':')) {
        "':' character not allowed in namespace."
    }
    require(!values.second.contains(':')) {
        "':' character not allowed in id."
    }
    return Identifier("${values.second}:${values.first}")
}

public fun id(namespace: String, id: String): Identifier {
    require(!namespace.contains(':')) {
        "':' character not allowed in namespace."
    }
    require(!id.contains(':')) {
        "':' character not allowed in id."
    }
    return Identifier("$namespace:$id")
}

public fun id(identifier: String): Identifier {
    require(identifier.matches(Regex("[^:]+:[^:]+"))) {
        "Identifier must contain two strings separated by a colon."
    }
    return Identifier(identifier)
}