/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.login

import org.karrat.packet.ServerboundPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readPrefixed

public class EncryptionResponsePacket(data: ByteBuffer) : ServerboundPacket {
    
    public val sharedSecret: ByteArray = data.readPrefixed()
    public val verifyToken: ByteArray = data.readPrefixed()
    
}