/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.packet.serverbound.ServerboundPacket

class PacketEvent<T : ServerboundPacket>(
    val packet: T,
    val session: Session
) : CancellableEvent() {
    
    override fun toString(): String =
        "PacketEvent(packet=$packet, session=$session)"
    
}