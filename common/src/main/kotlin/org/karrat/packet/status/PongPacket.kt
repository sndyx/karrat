/*
 * This file is part of Karrat.
 *
 * Karrat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Karrat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Karrat.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.karrat.packet.status

import org.karrat.packet.Clientbound
import org.karrat.packet.Packet
import org.karrat.util.ByteBuffer

@Clientbound
class PongPacket(val timestamp: Long) : Packet() {
    
    override val id = 0x01
    
    constructor(data: ByteBuffer) : this(
        timestamp = data.readLong()
    )
    
    override fun write(data: ByteBuffer) = data.writeLong(timestamp)
    
    
}