/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

public fun color(text: String): String {
    return text.replace(Regex("&([a-f0-9])")) { "§${it.groupValues[0]}" }
}