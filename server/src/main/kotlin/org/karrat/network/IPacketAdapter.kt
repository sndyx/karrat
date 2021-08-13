/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.packet.Packet
import org.karrat.util.ByteBuffer

interface IPacketAdapter {
    
    fun read(id: Int, data: ByteBuffer): Packet
    
}