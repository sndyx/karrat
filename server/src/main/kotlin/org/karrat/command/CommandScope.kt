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

    public fun respond(value: Any)
    
    public fun <T> argument(index: Int): T {
        return args.getValue(index)
    }

}

public class ConsoleCommandScope(
    override val args: CommandArguments,
) : CommandScope {

    override fun respond(value: Any) {
        println(value.toString().stripColor())
    }

}

public class PlayerCommandScope(
    override val args: CommandArguments,
    public val sender: Player,
) : CommandScope {

    override fun respond(value: Any) {
        sender.sendMessage(value.toString())
    }

}

public class CommandArguments internal constructor(
    args: List<Any>
) : List<Any> by args {

    @Suppress("Unchecked_Cast")
    public fun <T> getValue(index: Int): T {
        return get(index) as T
    }

}