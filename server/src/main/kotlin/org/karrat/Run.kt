/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import kotlin.system.exitProcess

private var i = 0

internal fun main(args: Array<String>) {
    while (i < args.size) {
        when (args[i]) {
            "--color-output", "-c" -> argumentColorOutput()
            "--help", "-h" -> argumentHelp()
            "--port", "-p" -> argumentPort(args)
            else -> printHelp()
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

private fun argumentPort(args: Array<String>) {
    val parsed = (args[i + 1].toShortOrNull())?.toInt()
    Config.port = if (parsed == null) {
        printHelp()
        Config.port
    } else {
        parsed
    }
    i++
}