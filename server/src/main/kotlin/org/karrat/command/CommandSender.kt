/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.entity.Player
import org.karrat.play.stripColor

public interface CommandSender {
    public fun respond(value: String)
}

public object ConsoleCommandSender : CommandSender {

    override fun respond(value: String) {
        println(value.stripColor())
    }

}

public class PlayerCommandSender(
    public val sender: Player,
) : CommandSender {

    override fun respond(value: String) {
        sender.sendMessage(value)
    }

}