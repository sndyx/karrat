/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command

internal fun Command.Companion.killCommand(): Command =
    command("kill") {
        argument<String>().onRun {
            Server.players
                .firstOrNull { it.name.equals(arg(0), ignoreCase = true) }
                    ?.remove()
                    ?: respond("Unable to find target.")
        }
    }.onRunByPlayer {
        sender.remove()
    }