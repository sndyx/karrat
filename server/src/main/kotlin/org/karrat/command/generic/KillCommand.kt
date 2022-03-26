/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.entity.Player

internal fun Command.CommandRegistry.killCommand(): Command =
    command("kill", "assassinate") {
        argument<String>().onRun {
            Server.players.first { it.name == args[0] }.remove()
        }
    }.onRunByPlayer {
        sender.remove()
    }