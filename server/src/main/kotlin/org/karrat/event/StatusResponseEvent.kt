/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.packet.serverbound.ServerboundPacket

class StatusResponseEvent(
    val session: Session
    var response: StatusResponse
) : CancellableEvent() {
    
    override fun toString(): String =
        "StatusResponseEvent(session=$session, response=$response)"
    
}
