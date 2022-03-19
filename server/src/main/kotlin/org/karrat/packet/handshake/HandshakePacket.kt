/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.packet.handshake

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readString
import org.karrat.struct.readUShort
import org.karrat.struct.readVarInt

public class HandshakePacket(data: ByteBuffer) : ServerboundPacket {

    public val protocol: Int = data.readVarInt()
    public val address: String = data.readString()
    public val port: UShort = data.readUShort()
    public val nextState: Int = data.readVarInt()

    override fun toString(): String =
        "HandshakePacket(protocol=$protocol, address=$address, port=$port," +
                " nextState=$nextState)"

}