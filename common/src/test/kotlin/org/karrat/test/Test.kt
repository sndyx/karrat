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