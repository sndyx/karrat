/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Server

internal fun Server.startConsoleInput() {
    runCatching {
        while (true) {
            println(readln())
        }
    }.onFailure {
        println("Exception in console thread! Stopping console input.")
    }
}