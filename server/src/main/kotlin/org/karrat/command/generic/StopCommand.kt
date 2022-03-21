/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.command

internal fun Command.Companion.stopCommand(): Command =
    command("stop").onRun {
        respond("Stopping server...")
        Server.stop()
    }