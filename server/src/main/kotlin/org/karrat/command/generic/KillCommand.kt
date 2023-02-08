/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.entity.Player

internal fun Command.CommandRegistry.killCommand(): Command =
    command("kill") {
        argument<String>().onRun {
            Server.players.firstOrNull { it.name == args.get<String>(0) }?.remove()
            ?: respond("No targets found.")
        }
    }.onRunByPlayer {
        sender.remove()
    }