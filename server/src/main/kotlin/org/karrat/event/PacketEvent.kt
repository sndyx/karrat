/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.packet.Packet

/**
 * A [CancellableEvent] that fires when a [Packet] is sent or received to/from a
 * [Session], represented as properties [packet] and [session] accordingly.
 *
 * The type [T] of [PacketEvent] can be set to only fire on a particular type of
 * [Packet]. eg: `PacketEvent<JoinGamePacket>`
 *
 * The network side of Minecraft is fragile: Cancelling packets should be done
 * safely, as cancellation of packets can lead to horrific errors.
 */
public class PacketEvent<T : Packet>(
    public val session: Session,
    public val packet: T,
) : CancellableEvent() {

    override fun toString(): String =
        "PacketEvent(session=$session, packet=$packet)"

}