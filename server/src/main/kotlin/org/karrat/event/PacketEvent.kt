/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session

/**
 * [Event] fired when a packet is sent or received by the server.
 */
class PacketEvent<T>(
    val session: Session,
    val packet: T
    ) : CancellableEvent() {
    
    override fun toString(): String =
        "PacketEvent(session=$session, packet=$packet)"
    
}