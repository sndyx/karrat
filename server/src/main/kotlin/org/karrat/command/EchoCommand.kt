/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

public fun Command.Companion.echoCommand(): Command =
    command("echo") {
        vararg<String>().onRun {
            respond(args.joinToString(" "))
        }
    }