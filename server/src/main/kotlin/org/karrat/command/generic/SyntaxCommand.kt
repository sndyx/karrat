/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.command.syntax

internal fun Command.CommandRegistry.syntaxCommand(): Command =
    command("syntax") {
        argument<String>().onRun {
            println(eval(args[0]).getOrNull()?.command?.syntax() ?: "Command not found.")
        }
    }