/*
 * This file is part of Karrat.
 *
 * Karrat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Karrat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Karrat.  If not, see <https://www.gnu.org/licenses/>.
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