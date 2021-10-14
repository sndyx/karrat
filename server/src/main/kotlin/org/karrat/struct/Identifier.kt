/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

public class Identifier(public val namespace: String, public val id: String) {
    override fun toString(): String = "$namespace:$id"
}