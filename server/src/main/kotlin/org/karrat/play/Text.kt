/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

public fun String.colored(): String {
    return replace(Regex("&([a-f0-9])")) { "§${it.groupValues[0]}" }
}

public fun String.stripColor(): String {
    return replace(Regex("§([a-f0-9])"), "")
}