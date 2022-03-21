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

        public fun run(command: String, sender: Sender? = null) {
            if (command.isEmpty()) {
                sender?.sendMessage(Config.commandNotFoundMessage)
            }
            val tokens = TokenBuffer(command.split(" "))
            val cmd = list.firstOrNull {
                (it as CommandLiteral)
                    .literal!!
                    .equals(tokens.get(), ignoreCase = Config.ignoreCommandCapitalization)
            }
            if (cmd != null) {
                cmd.run(tokens.consume(), CommandScope(sender, mutableListOf()))
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

    public fun execute(scope: CommandScope) {
        if (executor != null) {
            executor!!.invoke(scope)
        } else {
            scope.sendMessage(Config.invalidSyntaxMessage)
        }
    }

    public fun run(tokens: TokenBuffer, scope: CommandScope) {
        if (tokens.hasNext()) {
            val command = findSubCommand(tokens.get())
            if (command != null) {
                if (command is CommandArgument) {
                    command.run(
                        tokens.consume(),
                        scope.withArg(tokens.get())
                    )
                } else {
                    command.run(tokens.consume(), scope)
                }
            } else {
                scope.sendMessage(Config.invalidSyntaxMessage)
            }
        } else {
            execute(scope)
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

public inline fun <reified T> Command.vararg(
    label: String = T::class.simpleName ?: "Argument",
    structure: Command.() -> Unit = { }
): Command {
    val command = argument<T>(label, structure)
    command.subCommands.add(command)
    return command
}

public fun Command.redirect(command: Command) {
    subCommands.add(command)
}

public class TokenBuffer(
    public val list: List<String>
) {
    public var index: Int = 0

    public fun hasNext(): Boolean {
        return index < list.size
    }

    public fun hasNext(num: Int): Boolean {
        return index + num < list.size
    }

    public fun get(): String {
        return list[index]
    }

    public fun get(num: Int): String {
        return list[index + num]
    }

    public fun consume(): TokenBuffer {
        index++
        return this
    }

    public fun consume(num: Int): TokenBuffer {
        index += num
        return this
    }
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