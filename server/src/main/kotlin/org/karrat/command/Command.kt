/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Config
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
            register(installCommand())
            register(stopCommand())
            register(echoCommand())
        }

        // TODO convert to node
        public fun run(command: String, sender: Sender? = null) {
            if (command.isEmpty()) {
                sender?.sendMessage(Config.commandNotFoundMessage)
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
                sender?.sendMessage(Config.commandNotFoundMessage)
            }
        }

    }

    public val subCommands: MutableList<Command>

    public var executor: (CommandScope.() -> Unit)?

    public fun executes(executor: CommandScope.() -> Unit): Command {
        this.executor = executor
        return this
    }

    public fun execute(args: List<Any>, sender: Sender? = null) {
        val arguments = Arguments(args)
        if (executor != null) {
            executor!!.invoke(CommandScope(sender, arguments))
        } else {
            sender?.sendMessage(Config.invalidSyntaxMessage)
        }
    }

    public fun run(tokens: List<String>, sender: Sender? = null, args: List<Any>) {
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
                sender?.sendMessage(Config.invalidSyntaxMessage)
            }
        } else {
            execute(args, sender)
        }
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
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandArgument(T::class, label)
    command.structure()
    subCommands.add(command)
    return command
}

@PublishedApi
internal open class CommandLiteral @PublishedApi internal constructor(
    val literal: String?,
): Command {

    override val subCommands: MutableList<Command> = mutableListOf()

    override var executor: (CommandScope.() -> Unit)? = null
}

@PublishedApi
internal class CommandArgument @PublishedApi internal constructor (
    val type: KClass<*>,
    val label: String,
) : Command {
    override val subCommands: MutableList<Command> = mutableListOf()

    override var executor: (CommandScope.() -> Unit)? = null

}