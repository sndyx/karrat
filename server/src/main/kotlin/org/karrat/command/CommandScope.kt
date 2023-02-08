/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command

import org.karrat.entity.Player
import org.karrat.struct.Message
import org.karrat.struct.unformatted

@CommandDsl
public interface CommandScope {

    public val args: CommandArguments

    public companion object {
        public operator fun invoke(sender: Player?, args: CommandArguments): CommandScope {
            return if (sender != null) PlayerCommandScope(args, sender)
            else ConsoleCommandScope(args)
        }
    }

    public fun respond(message: Message)

}

public class ConsoleCommandScope(
    override val args: CommandArguments,
) : CommandScope {

    override fun respond(message: Message) {
        println(message.unformatted())
    }

}

public class PlayerCommandScope(
    override val args: CommandArguments,
    public val sender: Player,
) : CommandScope {

    override fun respond(message: Message) {
        sender.sendMessage(message)
    }

}

@Suppress("Unchecked_Cast")
public class CommandArguments internal constructor(
    private val args: List<Any>, private val mappings: List<String?>
) : Iterable<Any> {

    public operator fun <T> get(index: Int): T {
        return args[index] as T
    }
    
    public operator fun <T> get(key: String): T {
        return args[mappings.indexOf(key)] as T
    }
    
    override fun iterator(): Iterator<Any> = args.iterator()
    
}