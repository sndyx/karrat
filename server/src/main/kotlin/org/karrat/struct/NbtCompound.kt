/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable
import org.karrat.server.fatal

@Serializable
public class NbtCompound : LinkedHashMap<String, Any>() {

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
        check(
            value is Byte
                    || value is Short
                    || value is Int
                    || value is Long
                    || value is Float
                    || value is Double
                    || value is String
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
        if (entries.isNotEmpty()) builder.setLength(builder.length - 2)
        builder.append(')')
        return builder.toString()
    }

}

public fun Map<String, Any>.toNbtCompound(): NbtCompound =
    NbtCompound().also { it.putAll(this) }

public fun nbtOf(vararg pairs: Pair<String, Any>): NbtCompound =
    NbtCompound().also { it.putAll(pairs) }

public fun nbtOf(pair: Pair<String, Any>): NbtCompound =
    NbtCompound().also { it[pair.first] = pair.second }

@Suppress("Unchecked")
public fun <T> NbtCompound.getValue(key: String): T = get(key) as T

@Suppress("Unchecked")
public fun <T> NbtCompound.getValueOrDefault(key: String, default: T): T =
    get(key) as? T ?: default

public fun NbtCompound.getCompound(key: String): NbtCompound = getValue(key)