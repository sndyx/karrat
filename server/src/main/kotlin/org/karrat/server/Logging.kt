/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import org.karrat.Config
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

private val reset: String by lazy { if (Config.colorOutput) "\u001b[0m" else "" }
private val red: String by lazy { if (Config.colorOutput) "\u001b[38;5;9m" else "" }
private val yellow: String by lazy { if (Config.colorOutput) "\u001b[38;5;178m" else "" }

private val time: String
    get() {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return formatter.format(time)
    }

private val prefix: String
    get() {
        val caller = Thread.currentThread().stackTrace[3].fileName
        val format = "$time <$caller${
            if (Thread.currentThread().name == "main") ""
            else " @${Thread.currentThread().name}"
        }>"
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

public fun fatal(message: Any): Nothing {
    print(red)
    println("$prefix | fatal: $message\n")

    val stackTrace = Thread.currentThread().stackTrace

    for (n in 2..min(17, stackTrace.size)) {
        println("   @ " + stackTrace[n])
    }
    println()
    print(reset)
    throw RuntimeException(message.toString())
}