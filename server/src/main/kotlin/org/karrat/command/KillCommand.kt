/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Server

internal fun Command.Companion.killCommand(): Command =
    command("kill") {
        argument<String>().onRun {
            Server.players
                .firstOrNull { it.name.equals(args[0], ignoreCase = true) }
                    ?.remove()
                    ?: respond("Unable to find target.")
        }
    }.onRunByPlayer {
        sender.remove()
    }