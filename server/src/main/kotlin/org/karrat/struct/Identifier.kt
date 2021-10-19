/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

public class Identifier(public val namespace: String, public val id: String) {
    
    override fun toString(): String = "$namespace:$id"
    
}

public fun id(values: Pair<String, String>): Identifier =
    Identifier(values.second, values.first)

public fun id(namespace: String, id: String): Identifier =
    Identifier(namespace, id)

public fun id(identifier: String): Identifier =
    identifier.split(':').let { Identifier(it[0], it[1]) }