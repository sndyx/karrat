/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.play.colored

public class CommandExecutor {

    public var globalExecutor: (CommandScope.() -> Unit)? = null
    public var consoleExecutor: (ConsoleCommandScope.() -> Unit)? = null
    public var playerExecutor: (PlayerCommandScope.() -> Unit)? = null

    public fun execute(sender: CommandScope) {
        if (sender is ConsoleCommandScope && consoleExecutor != null) {
            consoleExecutor!!.invoke(sender)
        } else if (sender is PlayerCommandScope && playerExecutor != null) {
            playerExecutor!!.invoke(sender)
        } else if (globalExecutor != null) {
            globalExecutor!!.invoke(sender)
        } else {
            sender.respond("&cInvalid syntax".colored())
        }
    }

}