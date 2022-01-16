/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.entity.Player

/**
 * A [CancellableEvent] that fires whenever a [player] joins the server.
 * Cancelling this event will stop the player from joining the server.
 */
public class PlayerLoginEvent(public val player: Player) : CancellableEvent() {

    override fun toString(): String =
        "PlayerLoginEvent(player=$player)"

}