/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import java.text.SimpleDateFormat
import java.util.*

val time: String
get() {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(time)
}

val prefix: String
get() {
    val caller = Thread.currentThread().stackTrace[3].fileName
    val format = "$time <$caller${if (Thread.currentThread().name == "main") "" else " @${Thread.currentThread().name}"}>"
    return if (format.length > 40) format.substring(0, 36) + "...>"
    else format.padEnd(40)
}

fun info(message: Any) {
    println("$prefix | info: $message")
}

fun warning(message: Any) {
    println("$prefix | warn: $message")
}

fun fatal(message: Any): Nothing {
    println("$prefix | fatal: $message\n")
    for (n in 2..17) {
        println("   @ " + Thread.currentThread().stackTrace[n])
    }
    println()
    throw RuntimeException()
}