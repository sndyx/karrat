/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class ByteBufferKtTest {

    @Test
    fun readingAndWriting() {
        val buffer = DynamicByteBuffer()
        buffer.write(1)
        buffer.writeBoolean(true)
        buffer.writeShort(1000)
        buffer.writeInt(3000)
        buffer.writeLong(1000000L)
        buffer.writeFloat(2.7f)
        buffer.writeDouble(2.73777)
        buffer.writeUByte(1u)
        buffer.writeUShort(1000u)
        buffer.writeUInt(3000u)
        buffer.writeULong(1000000uL)
        buffer.writeVarInt(1329)
        buffer.writeVarLong(37129387L)
        buffer.writeIdentifier(Identifier("minecraft:test"))
        buffer.writeString("abcdefghjklmnopqrstuvwxyz")
        buffer.writeUuid(Uuid("bf8c08103dda48eca57343e162c0e79a"))
        //buffer.writeChatComponent(ChatComponent("S"))
        buffer.writeBytes(byteArrayOf(32, 10, 40, 32, 18))
        buffer.writePrefixed(byteArrayOf(32, 10, 40, 32, 18))
        
        assert(buffer.read() == (1).toByte())
        assert(buffer.readBoolean())
        assert(buffer.readShort() == (1000).toShort())
        assert(buffer.readInt() == 3000)
        assert(buffer.readLong() == 1000000L)
        assert(buffer.readFloat() == 2.7f)
        assert(buffer.readDouble() == 2.73777)
        assert(buffer.readUByte() == (1u).toUByte())
        assert(buffer.readUShort() == (1000u).toUShort())
        assert(buffer.readUInt() == 3000u)
        assert(buffer.readULong() == 1000000uL)
        assert(buffer.readVarInt() == 1329)
        assert(buffer.readVarLong() == 37129387L)
        assert(buffer.readIdentifier() == Identifier("minecraft:test"))
        assert(buffer.readString() == "abcdefghjklmnopqrstuvwxyz")
        assert(buffer.readUuid() == Uuid("bf8c08103dda48eca57343e162c0e79a"))
        //assert(buffer.readChatComponent() == ChatComponent("S"))
        assertArrayEquals(buffer.readBytes(5), byteArrayOf(32, 10, 40, 32, 18))
        assertArrayEquals(buffer.readPrefixed(), byteArrayOf(32, 10, 40, 32, 18))
    }
}