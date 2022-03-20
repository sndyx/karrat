/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Config
import org.karrat.entity.Player
import org.karrat.play.stripColor
import org.karrat.server.Loadable
import kotlin.reflect.KClass

public interface Command {

    public companion object : Loadable<Command> {

        override val list: MutableSet<Command> = mutableSetOf()

        override fun unregister(value: Command) {
            list.remove(value)
        }

        override fun register(value: Command) {
            list.add(value)
        }

        override fun load() {
            register(killCommand())
        }

        public fun run(command: String, sender: Player? = null) {
            if (command.isEmpty()) {
                sendCommandResponse(sender, Config.commandNotFoundMessage)
            }
            val tokens = command.split(" ")
            val cmd = list.firstOrNull {
                (it as CommandLiteral)
                    .literal!!
                    .equals(tokens[0], ignoreCase = Config.ignoreCommandCapitalization)
            }
            if (cmd != null) {
                cmd.run(tokens.drop(1), sender, emptyList())
            } else {
                sendCommandResponse(sender, Config.commandNotFoundMessage)
            }
        }

    }

    public val subCommands: MutableList<Command>

    public var globalExecutor: (CommandScope.() -> Unit)?
    public var playerExecutor: (PlayerCommandScope.() -> Unit)?
    public var consoleExecutor: (CommandScope.() -> Unit)?

    public fun onRun(executor: CommandScope.() -> Unit): Command {
        globalExecutor = executor
        return this
    }

    public fun onRunByPlayer(executor: PlayerCommandScope.() -> Unit): Command {
        playerExecutor = executor
        return this
    }

    public fun onRunByConsole(executor: CommandScope.() -> Unit): Command {
        consoleExecutor = executor
        return this
    }

}

public fun Command.run(tokens: List<String>, sender: Player? = null, args: List<Any>) {
    if (tokens.isNotEmpty()) {
        val command = findSubCommand(tokens[0])
        if (command != null) {
            if (command is CommandArgument) {
                command.run(
                    tokens.drop(1),
                    sender,
                    args.toMutableList().apply { add(tokens[0]) }
                )
            } else {
                command.run(tokens.drop(1), sender, args)
            }
        } else {
            sendCommandResponse(sender, Config.invalidSyntaxMessage)
        }
    } else {
        execute(args, sender)
    }
}

public inline fun Command.command(
    literal: String,
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandLiteral(literal)
    command.structure()
    subCommands.add(command)
    return command
}

public inline fun <reified T> Command.argument(
    label: String = T::class.simpleName ?: "Argument",
    completions: List<String> = emptyList(),
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandArgument(T::class, label, completions)
    command.structure()
    subCommands.add(command)
    return command
}

public fun CommandScope.respond(response: String) {
    if (this is PlayerCommandScope) {
        sender.sendMessage(response)
    } else {
        println(response)
    }
}

internal fun Command.execute(args: List<Any>, sender: Player? = null) {
    val arguments = Arguments(args)
    if (sender != null && playerExecutor != null) {
        playerExecutor!!.invoke(PlayerCommandScope(sender, arguments))
    } else if(sender == null && consoleExecutor != null) {
        consoleExecutor!!.invoke(CommandScope(arguments))
    } else if(globalExecutor != null) {
        globalExecutor!!.invoke(CommandScope(arguments))
    } else {
        sendCommandResponse(sender, Config.invalidSyntaxMessage)
    }
}

internal fun sendCommandResponse(sender: Player?, response: String) {
    if (sender != null) {
        sender.sendMessage(response)
    } else {
        println(response.stripColor())
    }
}

@PublishedApi
internal open class CommandLiteral @PublishedApi internal constructor(
    val literal: String?,
): Command {

    override val subCommands: MutableList<Command> = mutableListOf()

    override var globalExecutor: (CommandScope.() -> Unit)? = null
    override var playerExecutor: (PlayerCommandScope.() -> Unit)? = null
    override var consoleExecutor: (CommandScope.() -> Unit)? = null

}

@PublishedApi
internal class CommandArgument @PublishedApi internal constructor (
    val type: KClass<*>,
    val label: String,
    val completions: List<String>
) : CommandLiteral(null)