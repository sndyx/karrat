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

package org.karrat.packet.login

import org.karrat.packet.*
import org.karrat.util.*

@Suppress("ArrayInDataClass")
@Clientbound
data class EncryptionRequestPacket(
    val serverId: String,
    val publicKey: ByteArray,
    val verifyToken: ByteArray
    ) : Packet() {
    
    override val id = 0x01
    
    constructor(data: ByteBuffer) : this(
        serverId = data.readString(),
        publicKey = data.readPrefixed(),
        verifyToken = data.readPrefixed()
    )
    
    override fun write(data: ByteBuffer) = data.run {
        writeString(serverId)
        writePrefixed(publicKey)
        writePrefixed(verifyToken)
    }
    
}