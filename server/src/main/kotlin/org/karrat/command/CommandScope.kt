/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.entity.Player
import org.karrat.play.stripColor

@CommandDsl
public interface CommandScope {

    public val args: CommandArguments

    public companion object {
        public operator fun invoke(sender: Player?, args: List<Any>): CommandScope {
            return if (sender != null) PlayerCommandScope(CommandArguments(args), sender)
            else ConsoleCommandScope(CommandArguments(args))
        }
    }

    public fun respond(value: String)

}

public class ConsoleCommandScope(
    override val args: CommandArguments,
) : CommandScope {

    override fun respond(value: String) {
        println(value.stripColor())
    }

}

public class PlayerCommandScope(
    override val args: CommandArguments,
    public val sender: Player,
) : CommandScope {

    override fun respond(value: String) {
        sender.sendMessage(value)
    }

}

public class CommandArguments internal constructor(
    args: List<Any>
) : List<Any> by listOf(args) {

    public fun <T> getValue(index: Int): T {
        return get(index) as T
    }

}