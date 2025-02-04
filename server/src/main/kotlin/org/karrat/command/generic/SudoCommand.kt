/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command.generic

import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.command.vararg
import org.karrat.entity.Player

internal fun Command.CommandRegistry.sudoCommand(): Command =
    command("sudo") {
        argument<String> {
            vararg<String>().onRun {
                val player = Player(args.get<String>(0))
                val command = args.drop(1).joinToString(" ") { it as String }
                run(command, player)
            }
        }
    }