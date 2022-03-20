/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Config
import org.karrat.entity.Player
import org.karrat.play.stripColor
import kotlin.reflect.KClass

public inline fun command(literal: String, structure: Command.() -> Unit = { }): Command {
    val command = Command(literal)
    command.structure()
    return command
}

public fun CommandScope.respond(response: String) {
    if (this is PlayerCommandScope) {
        sender.sendMessage(response)
    } else {
        println(response)
    }
}

public open class Command(public val literal: String?) {

    public val subCommands: MutableList<Command> = mutableListOf()

    public var globalExecutor: (CommandScope.() -> Unit)? = null
    public var playerExecutor: (PlayerCommandScope.() -> Unit)? = null
    public var consoleExecutor: (CommandScope.() -> Unit)? = null

    public inline fun command(literal: String, structure: Command.() -> Unit = { }): Command {
        val command = Command(literal)
        command.structure()
        subCommands.add(command)
        return command
    }

    public inline fun <reified T> argument(
        label: String = T::class.simpleName ?: "Argument",
        completions: List<String> = emptyList(),
        structure: Command.() -> Unit = { }
    ): Command {
        val command = CommandArgument(T::class, label, completions)
        command.structure()
        subCommands.add(command)
        return command
    }

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

    public fun run(tokens: List<String>, sender: Player? = null, args: List<Any>) {
        if (tokens.isNotEmpty()) {
            var candidateSubCommand: CommandArgument? = null // Possible match if no better fit
            subCommands.first { subCommand ->  // Iterate through sub-commands.
                if (subCommand is CommandArgument) {
                    runCatching {
                        when (subCommand.type.qualifiedName) {
                            "kotlin.Boolean" -> tokens[0].lowercase().toBooleanStrict()
                            "kotlin.Byte" -> tokens[0].toByte()
                            "kotlin.Short" -> tokens[0].toShort()
                            "kotlin.Int" -> tokens[0].toInt()
                            "kotlin.Long" -> tokens[0].toLong()
                            "kotlin.Float" -> tokens[0].toFloat()
                            "kotlin.Double" -> tokens[0].toDouble()
                            "kotlin.String" -> {
                                candidateSubCommand = subCommand
                                return@first false
                                // Check other sub-commands for a match
                                // before defaulting to String argument.
                            }
                            else -> { return@first true } // Unexpected argument type.
                        }
                    }.onSuccess {
                        candidateSubCommand = subCommand
                    }
                } else if (subCommand.literal!!.equals(tokens[0], ignoreCase = true)) {
                    subCommand.run(tokens.drop(1), sender, args)
                }
                return@first false
            }
            if (candidateSubCommand != null) {
                candidateSubCommand!!.run(
                    tokens.drop(1),
                    sender,
                    args.toMutableList().apply { add(tokens[0]) }.toList()
                )
            } else {
                sendInvalidSyntaxMessage(sender)
            }
        } else {
            execute(args, sender)
        }
    }

    private fun sendInvalidSyntaxMessage(sender: Player?) {
        if (sender != null) {
            sender.sendMessage(Config.invalidSyntaxMessage)
        } else {
            println(Config.invalidSyntaxMessage.stripColor())
        }
    }

    private fun execute(args: List<Any>, sender: Player? = null) {
        val arguments = Arguments(args)
        if (sender != null && playerExecutor != null) {
            playerExecutor!!.invoke(PlayerCommandScope(sender, arguments))
        } else if(sender == null && consoleExecutor != null) {
            consoleExecutor!!.invoke(CommandScope(arguments))
        } else if(globalExecutor != null) {
            globalExecutor!!.invoke(CommandScope(arguments))
        } else {
            sendInvalidSyntaxMessage(sender)
        }
    }

}

public open class CommandScope(
    public val args: Arguments
)

public class PlayerCommandScope(
    public val sender: Player,
    args: Arguments
) : CommandScope(args)

public class Arguments(
    public val values: List<Any>,
) {

    public val size: Int
        get() = values.size

    public inline operator fun <reified T> get(index: Int): T {
        return values[index] as T
    }

}

public class CommandArgument(
    public val type: KClass<*>,
    public val label: String,
    public val completions: List<String>
) : Command(null)