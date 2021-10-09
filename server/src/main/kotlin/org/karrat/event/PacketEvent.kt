/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.packet.serverbound.ServerboundPacket

interface PacketEvent

/**
 * Event called when a packet is recieved from a client
 */
class ServerboundPacketEvent<T : ServerboundPacket>(
    val packet: T,
    val session: Session
) : CancellableEvent(), PacketEvent {
    
    override fun toString(): String =
        "ServerPacketEvent(packet=$packet, session=$session)"
}

/**
 * Event called when a packet is sent to a client
 */
class ClientboundPacketEvent<T : ClientboundPacket>(
    val packet: T,
    val session: Session
) : CancellableEvent(), PacketEvent {

    override fun toString(): String =
        "ClientPacketEvent(packet=$packet, session=$session)"
}