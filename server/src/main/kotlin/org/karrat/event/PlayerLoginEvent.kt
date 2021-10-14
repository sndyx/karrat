/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.entity.Player

/**
 * Called when the player logins
 */
public class PlayerLoginEvent(public val player: Player) : CancellableEvent() {

    public val kickReason: String = "You have been disconnected."

    override fun toString(): String =
        "PlayerLoginEvent(player=$player)"
    
}