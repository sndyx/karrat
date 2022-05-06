/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server.console

import kotlinx.coroutines.*
import org.karrat.Config
import org.karrat.Server
import org.karrat.command.Command

internal fun Server.registerConsoleOutput() {
    System.setOut(
        if (Config.basicLogging) {
            SimplePrintStream(System.out)
        } else {
            ReflectionPrintStream(System.out)
        }
    )
}

internal suspend fun Server.registerConsoleInput(): Unit = coroutineScope {
    runCatching {
        while (currentCoroutineContext().isActive) {
            val line = readln()
            Command.run(line, null)
            delay(1000L)
        }
    }.onFailure {
        if (it.message?.contains("EOF") == true) {
            println("Your terminal is not supported. Console commands will be ignored.")
        } else {
            println("Exception in console thread!")
            it.printStackTrace()
            registerConsoleInput()
        }
    }
}
