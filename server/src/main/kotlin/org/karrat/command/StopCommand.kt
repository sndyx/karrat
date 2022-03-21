/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Server

internal fun Command.Companion.stopCommand(): Command =
    command("stop").onRun {
        respond("Stopping server...")
        Server.stop()
    }