/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Server
import org.karrat.internal.exitProcessWithMessage

internal fun Server.startConsoleInput() {
    runCatching {
        while (true) {
            val line = readln()
            if (line == "stop") {
                exitProcessWithMessage("Stopping server...", 1)
            }
        }
    }.onFailure {
        println("Exception in console thread! Stopping console input.")
    }
}