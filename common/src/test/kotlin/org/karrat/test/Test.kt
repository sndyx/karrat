package org.karrat.test

import kotlinx.serialization.Serializable
import org.karrat.serialization.Nbt
import org.karrat.util.ByteBuffer

fun ByteArray.toHex(): String = joinToString(separator = " ") { eachByte -> "%02x".format(eachByte) }

fun main() {
    
    val test = Test(10.0)
    val nbt = Nbt.encodeToNbt(test)
    val bytes = Nbt.encodeToBytes(nbt)
    println(bytes.toHex())
    val buffer = ByteBuffer()
    buffer.writeInt(10)
    println(buffer.array().toHex())
}

@Serializable
class Test(val value: Double)