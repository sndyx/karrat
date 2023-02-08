/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.server.console

import kotlinx.coroutines.*
import org.karrat.Config
import org.karrat.Server
import org.karrat.command.Command

internal fun Server.setConsoleOutput() {
    System.setOut(
        if (Config.basicLogging) {
            SimplePrintStream(System.out)
        } else {
            ReflectionPrintStream(System.out)
        }
    )
}

internal suspend fun Server.startConsoleInput() {
    runCatching {
        while (isActive) {
            while (System.`in`.available() == 0) delay(50)
            val command = System.`in`.readNBytes(System.`in`.available()).decodeToString()
            Command.run(command.dropLast(1), null) // dropLast for newline ?
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
