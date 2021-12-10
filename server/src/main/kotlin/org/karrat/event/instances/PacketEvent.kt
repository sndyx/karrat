/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event.instances

import org.karrat.event.CancellableEvent
import org.karrat.network.Session
import org.karrat.packet.Packet

/*
 * [Event] When a packet is sent or received by the server.
 *
 * Parameters
 * - [Session] session, session who sends/receives packet
 * - [T : Packet] packet, relevant packet sent or received
 *
 * Can be cancelled
 * - Not sending the packet could horrifically break things
 *   that only run after another packet is received
 */
public class PacketEvent<T : Packet>(
    public val session: Session,
    public val packet: T,
) : CancellableEvent() {

    override fun toString(): String =
        "PacketEvent(session=$session, packet=$packet)"

}