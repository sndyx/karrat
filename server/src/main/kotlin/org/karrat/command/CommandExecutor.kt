/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.play.colored

public class CommandExecutor {

    public var globalExecutor: (CommandSender.() -> Unit)? = null
    public var consoleExecutor: (ConsoleCommandSender.() -> Unit)? = null
    public var playerExecutor: (PlayerCommandSender.() -> Unit)? = null

    public fun execute(sender: CommandSender) {
        if (sender is ConsoleCommandSender && consoleExecutor != null) {
            consoleExecutor!!.invoke(sender)
        } else if (sender is PlayerCommandSender && playerExecutor != null) {
            playerExecutor!!.invoke(sender)
        } else if (globalExecutor != null) {
            globalExecutor!!.invoke(sender)
        } else {
            sender.respond("&cInvalid syntax".colored())
        }
    }

}