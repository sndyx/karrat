/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import kotlin.system.exitProcess

private var i = 0

internal fun main(args: Array<String>) {
    while (i < args.size) {
        if (args[i].startsWith("--")) {
            when (args[i]) {
                "--color-output" -> argumentColorOutput()
                "--help" -> argumentHelp()
                "--port" -> argumentPort(args[i + 1])
                else -> printHelp()
            }
        } else if (args[i].startsWith('-')) {
            args[i].removePrefix("-").forEach {
                when (it) {
                    'c' -> argumentColorOutput()
                    'h' -> argumentHelp()
                    'p' -> argumentPort(args[i + 1])
                }
            }
        } else {
            printHelp()
        }
    }
    Server.start(Config.port)
}

private fun printHelp() {
    println(
        """
        Usage: karrat [options...]
         -c, --color-output    Color codes logging messages
         -h, --help            This help text
         -p, --port            Sets the port to listen on
    """.trimIndent()
    )
}

private fun argumentColorOutput() {
    Config.colorOutput = true
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