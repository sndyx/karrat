/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.PacketEvent
import org.karrat.event.dispatchEvent
import org.karrat.network.handler.NetHandlerHandshake
import org.karrat.network.translation.cipher
import org.karrat.network.translation.compress
import org.karrat.network.translation.decipher
import org.karrat.network.translation.decompress
import org.karrat.packet.ClientboundPacket
import org.karrat.packet.login.SetCompressionPacket
import org.karrat.packet.play.DisconnectPacket
import org.karrat.struct.*
import java.net.SocketAddress
import javax.crypto.Cipher

public class Session(public val socket: SocketChannel) {

    public val address: SocketAddress
        get() = socket.remoteAddress

    /**
     * Player linked to this session instance, if in state Play.
     */
    public lateinit var player: Player
    public val isAlive: Boolean
        get() = socket.isOpen

    public var netHandler: NetHandler = NetHandlerHandshake(this)

    public var isCompressionEnabled: Boolean = false
    public var isEncryptionEnabled: Boolean = false

    /**
     * Pair of encryption and decryption ciphers used for packet encryption.
     * Only initialized once [isEncryptionEnabled] is true.
     */
    public lateinit var ciphers: Pair<Cipher, Cipher>

    public fun send(packet: ClientboundPacket) { // Packet -> Encoder -> Compress -> Prepended -> Encrypt -> Bytes
        if (Server.dispatchEvent(PacketEvent(this, packet))) return
        val buffer = DynamicByteBuffer()
        buffer.writeVarInt(packet.id)
        packet.write(buffer)
        if (isCompressionEnabled) compress(buffer)
        val prefixedBuffer = MutableByteBuffer(
            buffer.size // Prepend
                    + varSizeOf(buffer.size)
        )
        prefixedBuffer.writePrefixed(buffer.array())
        if (isEncryptionEnabled) cipher(prefixedBuffer) // Encrypt
        socket.write(prefixedBuffer)
    }

    public fun disconnect(reason: String) {
        send(DisconnectPacket(TextComponent(reason)))
        socket.close()
    }

    public fun handle() { // Bytes -> Decrypt -> Splitter -> Decompress -> Decoder -> Packet
        val buffer = socket.read()
        if (buffer.size != 0) {
            if (isEncryptionEnabled) decipher(buffer)
            if (buffer.read() == 0xfe.toByte() && buffer.read() == 0x01.toByte()) { // Check for legacy packet
                handleLegacyPacket() // Get out of here, you fool!!!
                return
            }
            buffer.reset()
            while (buffer.remaining != 0) { // Splitting and decompressing
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                if (isCompressionEnabled) decompress(payload)
                val id = payload.readVarInt() // Decoding
                val packet = netHandler.read(id, payload)
                if (Server.dispatchEvent(PacketEvent(this, packet)))
                    continue
                netHandler.process(packet)
            }
        }
    }

    public fun enableEncryption(encryptCipher: Cipher, decryptCipher: Cipher) {
        isEncryptionEnabled = true
        ciphers = Pair(encryptCipher, decryptCipher)
    }

    public fun enableCompression() {
        isCompressionEnabled = true
        send(SetCompressionPacket(Config.compressionThreshold))
    }

    override fun toString(): String =
        "Session(address=$address)"

}