/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.Command
import org.karrat.command.command
import org.karrat.command.vararg

public fun Command.CommandRegistry.echoCommand(): Command =
    command("echo") {
        vararg<String>().onRun {
            respond(args.get<List<String>>(0).joinToString(" "))
        }
    }