/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.item

import kotlinx.serialization.Serializable

@Serializable
class NbtCompound : LinkedHashMap<String, Any>() {
    
    override fun toString(): String {
        val builder = StringBuilder("NbtCompound(")
        entries.forEach {
            builder.append(it.key).append('=')
            if (it.value is String) builder.append("\"")
            builder.append(it.value.toString())
            if (it.value is String) builder.append("\"")
            builder.append(", ")
        }
        builder.setLength(builder.length - 2)
        builder.append(')')
        return builder.toString()
    }
    
}

fun MutableMap<String, Any>.toNbtCompound() = NbtCompound().also { it.putAll(this) }