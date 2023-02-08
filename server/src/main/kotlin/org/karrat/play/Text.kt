/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.play

public fun String.colored(): String {
    return replace(Regex("(?!(?:(\\\\){2})*\\\\)&([a-f\\d])")) { "§${it.groupValues[0]}" }
}

public fun String.stripColor(): String {
    return replace(Regex("§([a-f\\d])"), "")
}