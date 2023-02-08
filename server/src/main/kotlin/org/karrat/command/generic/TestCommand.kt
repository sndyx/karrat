/*
 * Copyright © Karrat - 2023.
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