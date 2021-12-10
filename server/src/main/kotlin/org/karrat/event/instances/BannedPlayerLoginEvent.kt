/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event.instances

import org.karrat.event.CancellableEvent
import org.karrat.struct.Uuid

/*
 * [Event] When a player is banned
 *
 * Parameters
 * - [Uuid] uuid, the player's uuid
 * - [String] username, the player's username
 * - [c] [String] message, the ban message
 *
 * Can be cancelled
 */
public class BannedPlayerLoginEvent(public val uuid: Uuid, public val username: String) : CancellableEvent() {

    public var message: String = "You are banned from this server."

    override fun toString(): String =
        "BannedPlayerLoginEvent(uuid=$uuid)"

}