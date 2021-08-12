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

package org.karrat.test

import kotlinx.serialization.Serializable
import org.karrat.serialization.Nbt

fun ByteArray.toHex(): String = joinToString(separator = " ") { eachByte -> "%02x".format(eachByte) }

fun main() {
    val mainWorld = World("MAIN_WORLD")
    val location = Location(10.0, 84.30321, 582.05, mainWorld)
    println("Original: $location")
    val nbt = Nbt.encodeToNbt(location)
    println("Nbt: $nbt")
    val bytes = Nbt.encodeToBytes(nbt)
    println("Bytes: ${bytes.toHex()}")
    val recovered = Nbt.decodeFromNbt<Location>(Nbt.decodeFromBytes(bytes))
    println("Recovered: $recovered")
}

@Serializable
data class Location(val x: Double, val y: Double, val z: Double, val world: World)

@Serializable
data class World(val name: String)