/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.struct.Uuid

public class BannedPlayerLoginEvent(public val uuid: Uuid) : CancellableEvent() {
    
    public var message: String = "You are banned from this server."
    
    override fun toString(): String =
        "BannedPlayerLoginEvent(uuid=$uuid)"
    
}