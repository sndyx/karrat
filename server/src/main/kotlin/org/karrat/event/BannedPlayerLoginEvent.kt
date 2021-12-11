/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.struct.Uuid

/**
 * A [CancellableEvent] fired when a banned player attempts to log in. By
 * default, the player will be blocked from logging in and receive a [message].
 * If [isCancelled], the player will be able to log onto the server as usual.
 */
public class BannedPlayerLoginEvent(public val uuid: Uuid, public val username: String) : CancellableEvent() {

    public var message: String = "You are banned from this server."

    override fun toString(): String =
        "BannedPlayerLoginEvent(uuid=$uuid)"

}