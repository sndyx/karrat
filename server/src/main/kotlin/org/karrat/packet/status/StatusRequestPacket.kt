/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.status

import org.karrat.packet.ServerboundPacket

public object StatusRequestPacket : ServerboundPacket {

    override fun toString(): String = "StatusRequestPacket"

}