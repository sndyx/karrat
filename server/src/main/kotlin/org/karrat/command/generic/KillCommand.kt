/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.entity.Player

internal fun Command.Companion.killCommand(): Command =
    command("kill") {
        argument<String>().onRun {
            val player = args[0]

            if (player is Player) {
                (args[0] as Player).remove()
            } else {
                respond("Unable to find target.")
            }
        }
    }.onRun {
        if (sender is Player) {
            (sender as Player).remove()
        }
    }