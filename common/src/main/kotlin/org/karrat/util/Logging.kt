/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.util

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
    return "$time [${Thread.currentThread().name}]"
}

fun log(message: String) {
    println("$prefix - info: $message")
}

fun warning(message: String) {
    println("$prefix - warn: $message")
}

fun fatal(message: String) {
    println("$prefix - fatal: $message")
}