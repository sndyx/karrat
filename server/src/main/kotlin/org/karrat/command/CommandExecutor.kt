/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.play.colored

public class CommandExecutor {

    public var globalExecutor: (CommandScope<CommandSender>.() -> Unit)? = null
    public var consoleExecutor: (CommandScope<ConsoleCommandSender>.() -> Unit)? = null
    public var playerExecutor: (CommandScope<PlayerCommandSender>.() -> Unit)? = null

    public fun execute(scope: CommandScope<CommandSender>) {
        if (scope.sender is ConsoleCommandSender && consoleExecutor != null) {
            consoleExecutor!!.invoke(scope as CommandScope<ConsoleCommandSender>)
        } else if (scope.sender is PlayerCommandSender && playerExecutor != null) {
            playerExecutor!!.invoke(scope as CommandScope<PlayerCommandSender>)
        } else if (globalExecutor != null) {
            globalExecutor!!.invoke(scope)
        } else {
            scope.respond("&cInvalid Syntax".colored())
        }
    }
}