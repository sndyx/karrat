/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.ConsoleCommandScope

internal fun Server.startConsoleInput() {
    runCatching {
        while (true) {
            val line = readln()
            Command.run(line, ConsoleCommandScope)
        }
    }.onFailure {
        if (it.message?.contains("EOF") == true) {
            println("Your terminal is not supported. Console commands will be ignored.")
        } else {
            println("Exception in console thread!")
            it.printStackTrace()
            startConsoleInput()
        }
    }
}