/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.struct.Uuid

/*
    Event that fires when a player is banned

    - Provided Info
    - uuid, which is the player's uuid
    - username, which is the player's username
    - message, which is the default ban message and can be changed

    - When Cancelled: player is not banned
 */
public class BannedPlayerLoginEvent(public val uuid: Uuid, public val username: String) : CancellableEvent() {
    
    public var message: String = "You are banned from this server."
    
    override fun toString(): String =
        "BannedPlayerLoginEvent(uuid=$uuid)"
    
}