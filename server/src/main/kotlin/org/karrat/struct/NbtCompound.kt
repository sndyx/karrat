/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable
import org.karrat.server.fatal

@Serializable
class NbtCompound : LinkedHashMap<String, Any>() {
    
    override fun put(key: String, value: Any): Any? {
        checkType(value)
        return super.put(key, value)
    }
    
    private fun checkType(value: Any) {
        if (value is List<*>) {
            value.firstOrNull()?.let {
                checkType(it)
            }
            return
        }
        check(value is Byte
            || value is Short
            || value is Int
            || value is Long
            || value is Float
            || value is Double
            || value is ByteArray
            || value is NbtCompound
            || value is IntArray
            || value is LongArray
        ) { fatal("NbtCompound does not accept type ${value::class.simpleName}.") }
    }
    
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