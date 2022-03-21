/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.Config
import org.karrat.entity.Player

public inline fun command(
    literal: String,
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandLiteral(literal)
    command.structure()
    return command
}

internal fun Command.findSubCommand(token: String): Command? {
    var possibleStringArgumentNode: CommandArgument? = null
    subCommands.forEach { command ->
        if (command is CommandArgument) {
            runCatching {
                when (command.type.qualifiedName) {
                    "kotlin.Boolean" -> token.lowercase().toBooleanStrict()
                    "kotlin.Byte" -> token.toByte()
                    "kotlin.Short" -> token.toShort()
                    "kotlin.Int" -> token.toInt()
                    "kotlin.Long" -> token.toLong()
                    "kotlin.Float" -> token.toFloat()
                    "kotlin.Double" -> token.toDouble()
                    "kotlin.String" -> {
                        possibleStringArgumentNode = command
                        return@forEach
                        // Check other sub-commands for a match
                        // before defaulting to String argument.
                    }
                    else -> { return@forEach } // Unexpected argument type.
                }
            }.onSuccess {
                return command
            }
        } else if (command is CommandLiteral) {
            if (command.literal!!.equals(token, ignoreCase = Config.ignoreCommandCapitalization)) {
                return command
            }
        }
        return@forEach
    }
    return possibleStringArgumentNode
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