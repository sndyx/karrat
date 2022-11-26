/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.struct.Message

public open class CommandExecutor {

    public var globalExecutor: (CommandScope.() -> Unit)? = null
    public var consoleExecutor: (ConsoleCommandScope.() -> Unit)? = null
    public var playerExecutor: (PlayerCommandScope.() -> Unit)? = null

    public open fun execute(sender: CommandScope) {
        runCatching {
            if (sender is ConsoleCommandScope && consoleExecutor != null) {
                consoleExecutor!!.invoke(sender)
            } else if (sender is PlayerCommandScope && playerExecutor != null) {
                playerExecutor!!.invoke(sender)
            } else if (globalExecutor != null) {
                globalExecutor!!.invoke(sender)
            } else {
                sender.respond(Message("&cInvalid syntax."))
            }
        }.onFailure {
            sender.respond("&cError: ${it.message}")
            it.printStackTrace()
        }
    }

}