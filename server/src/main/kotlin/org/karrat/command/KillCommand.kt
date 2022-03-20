/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Server

internal fun Command.killCommand(): Command =
    command("kill") {
        argument<String>().onRun {
            Server.players
                .first { it.name.equals(args[0], ignoreCase = true) }
                .remove()
        }
    }.onRunByPlayer {
        sender.remove()
    }