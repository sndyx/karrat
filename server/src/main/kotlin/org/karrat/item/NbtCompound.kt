/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.item

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NbtCompound(val elements: MutableMap<String, @Contextual Any> = mutableMapOf()) {
    
    inline fun <reified T> get(key: String) = elements[key] as T
    
    fun put(key: String, value: Any) {
        elements[key] = value
    }
    
    override fun toString(): String {
        val builder = StringBuilder("NbtCompound(")
        elements.forEach {
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