/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.command

internal fun Command.CommandRegistry.stopCommand(): Command =
    command("stop", "exit").onRun {
        respond("Stopping server...")
        Server.stop()
    }