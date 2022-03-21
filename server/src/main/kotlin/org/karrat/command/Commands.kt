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
    public val sender: Sender?,
    public val args: Arguments
) {
    public fun sendMessage(response: String) {
        sender?.sendMessage(response)
    }
}

public abstract class Sender {
    public abstract fun sendMessage(response: String)
}

public class PlayerSender(
    public val sender: Player,
) : Sender() {
    override fun sendMessage(response: String) {
        sender.sendMessage(response)
    }
}

public object ConsoleSender : Sender() {
    override fun sendMessage(response: String) {
        println("Command response to console: \n$response")
    }
}

public class Arguments(
    public val values: List<Any>,
) {

    public val size: Int
        get() = values.size

    public inline operator fun <reified T> get(index: Int): T {
        return values[index] as T
    }

}