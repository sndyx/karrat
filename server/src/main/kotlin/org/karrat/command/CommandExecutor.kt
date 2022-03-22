/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.play.colored

public class CommandExecutor {

    public var executor: (CommandScope.() -> Unit)? = null

    public fun execute(scope: CommandScope) {
        executor?.let { it(scope) } ?: scope.respond("&No executor".colored())
    }

}