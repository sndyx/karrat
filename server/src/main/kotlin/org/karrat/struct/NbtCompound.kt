/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable

@Serializable
class NbtCompound : LinkedHashMap<String, Any>() {
    
    override fun toString(): String {
        val builder = StringBuilder("NbtCompound(")
        entries.forEach {
            builder.append(it.key).append('=')
            builder.append(it.value.toString())
            builder.append(", ")
        }
        builder.setLength(builder.length - 2)
        builder.append(')')
        return builder.toString()
    }
    
}

fun Map<String, Any>.toNbtCompound() = NbtCompound().also { it.putAll(this) }

fun nbtOf(vararg pairs: Pair<String, Any>): NbtCompound =
    NbtCompound().also { it.putAll(pairs) }

fun nbtOf(pair: Pair<String, Any>): NbtCompound =
    NbtCompound().also { it[pair.first] = pair.second }

@Suppress("Unchecked_Cast")
fun <T> NbtCompound.getAs(key: String): T = get(key) as T