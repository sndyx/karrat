/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event.instances

import org.karrat.entity.Player
import org.karrat.event.CancellableEvent

/*
 * [Event] When a player logins, meaning that the login packets will be sent after
 *
 * Parameters
 * - [Player] player, relevant player
 *
 * Can be cancelled
 */
public class PlayerLoginEvent(public val player: Player) : CancellableEvent() {

    override fun toString(): String =
        "PlayerLoginEvent(player=$player)"
    
}