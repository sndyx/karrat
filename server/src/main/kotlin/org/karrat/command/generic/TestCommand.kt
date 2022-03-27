/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.*

internal fun Command.CommandRegistry.testCommand(): Command =
    command("test") {
        route("redirect") {
            redirect(Command.Root)
        }
        route("redirectUnsafe") {
            redirect(Command.Root)
            route("to") {
                redirect(Command.Root)
            }
        }
    }