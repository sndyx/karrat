/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.packet.serverbound.ServerboundPacket
import java.util.*

class PacketEvent<T : ServerboundPacket>(
    val packet: T,
    val sender: UUID?
) : CancellableEvent()