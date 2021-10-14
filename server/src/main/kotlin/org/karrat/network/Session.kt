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
import org.karrat.packet.ClientboundPacket
import org.karrat.packet.login.clientbound.SetCompressionPacket
import org.karrat.packet.play.DisconnectPacket
import org.karrat.play.ChatComponent
import org.karrat.server.info
import org.karrat.struct.*
import java.net.SocketAddress
import java.nio.channels.SocketChannel
import javax.crypto.Cipher

public class Session(public val socket: SocketChannel) {
    
    public val address: SocketAddress = socket.remoteAddress
    
    /**
     * Player linked to this session instance, if in state Play.
     */
    public lateinit var player: Player
    public val isAlive: Boolean get() = socket.isOpen
    
    public var netHandler: NetHandler = NetHandlerHandshake(this)
    
    public var isCompressionEnabled: Boolean = false
    public var isEncryptionEnabled: Boolean = false
    
    /**
     * Pair of encryption and decryption ciphers used for packet encryption.
     * Only initialized once [isEncryptionEnabled] is true.
     */
    public lateinit var ciphers: Pair<Cipher, Cipher>
    
    public fun send(packet: ClientboundPacket) { // (Packet) -> (Encoder - format into bytes) -> (Compress) -> (Prepended) -> (Encrypt) -> Bytes
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
        info("Sent Packet: ${packet.javaClass.name}")
        info("Encoded: $prefixedBuffer")
        info("Decoded: ${prefixedBuffer.array().decodeToString()}")
    }
    
    public fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    public fun handle() { // Bytes -> (Decrypt) -> (Splitter) -> (Decompress) -> (Decoder - format into packet) -> (Packet)
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
                info(buffer)
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                info(payload)
                if (isCompressionEnabled) decompress(payload)
                val id = payload.readVarInt() // Decoding
                val packet = netHandler.read(id, payload)
                if (Server.dispatchEvent(PacketEvent(this, packet)))
                    continue

                info("Recieved Packet: ${packet.javaClass.name}")
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
        send(SetCompressionPacket(Config.compressionThreshold)) // TODO: Read value from config
    }
    
    override fun toString(): String =
        "Session(address=$address)"
    
}