/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login.serverbound

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readString

class LoginStartPacket(data : ByteBuffer) : ServerboundPacket {

    val username = data.readString();

}