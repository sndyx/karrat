/*
 * Copyright © Karrat - 2023.
 */

package org.karrat

import kotlin.system.exitProcess

private var i = 0

internal fun main(args: Array<String>) {
    while (i < args.size) {
        if (args[i].startsWith("--")) {
            when (args[i]) {
                "--help" -> argumentHelp()
                "--port" -> argumentPort(args[i + 1])
                "--basic-logging" -> argumentBasicLogging()
                "--dev-env" -> argumentDevEnv()
                else -> {
                    println("Unable to parse argument ${args[i]}")
                    argumentHelp()
                }
            }
        } else if (args[i].startsWith('-')) {
            args[i].removePrefix("-").forEach {
                when (it) {
                    'h' -> argumentHelp()
                    'p' -> argumentPort(args[i + 1])
                    'd' -> argumentDevEnv()
                    else -> {
                        println("Unable to parse single char parameter $it")
                        argumentHelp()
                    }
                }
            }
        } else {
            println("Unable to parse input ${args[i]}")
            argumentHelp()
            return
        }
        i++
    }
    Server.start()
}

private fun printHelp() {
    println(
        """
        Usage: karrat [options...]
         -h, --help          This help text
         -p, --port          Sets the port to listen on
         -d, --dev-env       Enables developer environment
             --basic-logging Disables reflection in logging
    """.trimIndent()
    )
}

private fun argumentHelp() {
    printHelp()
    exitProcess(0)
}

private fun argumentPort(next: String) {
    val parsed = next.toShortOrNull()?.toInt()
    Config.port = if (parsed == null) {
        printHelp()
        Config.port
    } else {
        parsed
    }
    i++
}

private fun argumentBasicLogging() {
    Config.basicLogging = true
}

private fun argumentDevEnv() {
    Config.isDevEnvironment = true
}