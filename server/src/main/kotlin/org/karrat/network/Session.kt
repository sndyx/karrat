/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.PacketEvent
import org.karrat.event.dispatchEvent
import org.karrat.internal.NioByteBuffer
import org.karrat.network.handlers.NetHandler
import org.karrat.network.handlers.NetHandlerHandshake
import org.karrat.network.pipeline.*
import org.karrat.network.pipeline.cipher
import org.karrat.network.pipeline.decipher
import org.karrat.network.pipeline.decompress
import org.karrat.network.pipeline.handleLegacyPacket
import org.karrat.packet.ClientboundPacket
import org.karrat.packet.login.SetCompressionPacket
import org.karrat.packet.play.DisconnectPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.*
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import javax.crypto.Cipher

public class Session(public val socket: SocketChannel) {
    
    public val address: SocketAddress = socket.remoteAddress
    
    /**
     * Player linked to this session instance, if in state Play.
     */
    public var player: Player? = null
    public val isAlive: Boolean get() = socket.isOpen
    
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
        val prefixedBuffer = MutableByteBuffer(buffer.size // Prepend
                + varSizeOf(buffer.size))
        prefixedBuffer.writePrefixed(buffer.array())
        if (isEncryptionEnabled) cipher(prefixedBuffer) // Encrypt
        val nioBuffer = prefixedBuffer.nio()
        nioBuffer.flip()
        socket.write(nioBuffer)
    }
    
    public fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    public fun handle() { // Bytes -> Decrypt -> Splitter -> Decompress -> Decoder -> Packet
        val nioBuffer = NioByteBuffer.allocate(1028)
        socket.read(nioBuffer)
        val buffer = nioBuffer.array().copyOf(1028 - nioBuffer.remaining()).toByteBuffer()
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