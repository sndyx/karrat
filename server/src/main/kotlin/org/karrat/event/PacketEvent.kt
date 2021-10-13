/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.packet.Packet

/**
 * [Event] fired when a packet is sent or received by the server.
 */
public class PacketEvent<T : Packet>(
    public val session: Session,
    public val packet: T,
) : CancellableEvent() {
    
    override fun toString(): String =
        "PacketEvent(session=$session, packet=$packet)"
    
}