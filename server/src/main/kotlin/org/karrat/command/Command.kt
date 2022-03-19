/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import kotlin.reflect.KClass

public inline fun command(literal: String, structure: Command.() -> Unit = { }): Command {
    val command = Command(literal)
    command.structure()
    return command
}

public fun CommandScope.respond(response: String) {
    // sender.sendMessage(response)
}

public open class Command(public val literal: String) {

    public val subCommands: MutableList<Command> = mutableListOf()
    public var executor: CommandScope.() -> Unit = { respond("Invalid syntax.") }

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

    public fun run(executor: CommandScope.() -> Unit) {
        this.executor = executor
    }

}

public open class CommandScope(
    public val args: Arguments,
)

public class Arguments(
    public val values: List<Any>,
) {

    public val size: Int
        get() = values.size

    public inline fun <reified T> get(index: Int): T {
        return values[index] as T
    }

}

public class CommandArgument(
    type: KClass<*>,
    label: String,
    completions: List<String>
) : Command("\$literal")