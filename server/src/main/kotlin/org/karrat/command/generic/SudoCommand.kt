/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.Server
import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.command.redirect

internal fun Command.CommandRegistry.sudoCommand(): Command =
    command("sudo") {
        argument<String> {
            redirect(Command.CommandRegistry) {
                // TODO set sender 
            }
        }
    }