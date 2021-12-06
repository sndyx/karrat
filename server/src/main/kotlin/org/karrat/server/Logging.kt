/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

private const val reset = "\u001B[0m"
private const val red = "\u001b[38;5;9m"
private const val yellow = "\u001b[38;5;178m"

private val time: String
get() {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(time)
}

private val prefix: String
get() {
    val caller = Thread.currentThread().stackTrace[3].fileName
    val format = "$time <$caller${if (Thread.currentThread().name == "main") ""
    else " @${Thread.currentThread().name}"}>"
    return if (format.length > 40) format.substring(0, 36) + "...>"
    else format.padEnd(40)
}

public fun info(message: Any) {
    println("$prefix | info: $message")
}

public fun warning(message: Any) {
    print(yellow)
    println("$prefix | warn: $message")
    print(reset)
}

public fun fatal(message: Any) : Nothing {
    print(red)
    println("$prefix | fatal: $message\n")

    val stackTrace = Thread.currentThread().stackTrace

    for (n in 2..min(17, stackTrace.size)) {
        println("   @ " + stackTrace[n])
    }
    println()
    print(reset)
    throw RuntimeException()
}