/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.karrat.play.ChatComponent

internal class ByteBufferKtTest {
    
    @Test
    fun readBytes() {
        val buffer = byteBufferOf(15, 39, 20, 19, 12, 19)
        val bytes = buffer.readBytes()
        assertArrayEquals(byteArrayOf(15, 39, 20, 19, 12, 19), bytes)
    }

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
        buffer.writePrefixed(byteArrayOf(32, 10, 40, 32, 18))
        buffer.writeVarInt(1329)
        buffer.writeVarLong(37129387L)
        buffer.writeIdentifier(Identifier("minecraft:test"))
        buffer.writeString("abcdefghjklmnopqrstuvwxyz")

        val randomUuid = Uuid.random()

        buffer.writeUuid(randomUuid)
        //buffer.writeChatComponent(ChatComponent("S"))

        assert(buffer.read() == (1).toByte())
        assert(buffer.readBoolean())
        assert(buffer.readShort() == (1000).toShort())
        assert(buffer.readInt() == 3000)
        assert(buffer.readLong() == 1000000L)
        assert(buffer.readFloat() == 2.7f)
        assert(buffer.readDouble() == 2.7377)
        assert(buffer.readUByte() == (1u).toUByte())
        assert(buffer.readUShort() == (1000u).toUShort())
        assert(buffer.readUInt() == 3000u)
        assert(buffer.readULong() == 1000000uL)
        assertArrayEquals(buffer.readPrefixed(), byteArrayOf(32, 10, 40, 32, 18))
        assert(buffer.readVarInt() == 1329)
        assert(buffer.readVarLong() == 37129387L)
        assert(buffer.readIdentifier() == Identifier("minecraft:test"))
        assert(buffer.readString() == "abcdefghjklmnopqrstuvwxyz")
        assert(buffer.readUuid() == randomUuid)
        //assert(buffer.readChatComponent() == ChatComponent("S"))
    }
}