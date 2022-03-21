/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.entity.Player
import org.karrat.play.stripColor

public interface CommandSender {

    public val args: ArrayDeque<Any>

    public fun respond(value: String)

    public companion object {

        public fun of(sender: Player?, args: List<Any>): CommandSender {
            return if (sender == null) {
                ConsoleCommandSender(ArrayDeque(args))
            } else {
                PlayerCommandSender(ArrayDeque(args), sender)
            }
        }

    }

    @Suppress("Unchecked_Cast")
    public fun <T> arg(index: Int): T {
        return args[index] as T
    }

}

public class ConsoleCommandSender(
    override val args: ArrayDeque<Any>,
) : CommandSender {

    override fun respond(value: String) {
        println(value.stripColor())
    }

}

public class PlayerCommandSender(
    override val args: ArrayDeque<Any>,
    public val sender: Player,
) : CommandSender {

    override fun respond(value: String) {
        sender.sendMessage(value)
    }

}