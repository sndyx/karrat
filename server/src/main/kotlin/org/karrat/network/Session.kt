/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.PacketEvent
import org.karrat.event.dispatchEvent
import org.karrat.packet.ClientboundPacket
import org.karrat.packet.login.clientbound.SetCompressionPacket
import org.karrat.packet.play.DisconnectPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.*
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.Socket
import javax.crypto.Cipher

public class Session(public val socket: Socket) {
    
    /**
     * Player linked to this session instance, if in state Play.
     */
    public val player: Player? = null
    public val isAlive: Boolean get() = !socket.isClosed
    
    public val readChannel: BufferedInputStream = socket.getInputStream().buffered()
    public val writeChannel: OutputStream by lazy { socket.getOutputStream() }
    
    public var netHandler: NetHandler = NetHandlerHandshake(this)
    
    public var isCompressed: Boolean = false
    public var isEncrypted: Boolean = false
    
    /**
     * Pair of encryption and decryption ciphers used for packet encryption.
     * Only initialized once [isEncrypted] is true.
     */
    public lateinit var ciphers: Pair<Cipher, Cipher>
    
    public fun send(packet: ClientboundPacket) { // (Packet) -> (Encoder - format into bytes) -> (Compress) -> (Prepended) -> (Encrypt) -> Bytes
        if (Server.dispatchEvent(PacketEvent(this, packet))) return
        val buffer = DynamicByteBuffer()
        buffer.writeVarInt(packet.id)
        packet.write(buffer)
        if (isCompressed) compress(buffer)
        val prefixedBuffer = MutableByteBuffer(buffer.size // Prepend
                + varSizeOf(buffer.size))
        prefixedBuffer.writePrefixed(buffer.array())
        if (isEncrypted) cipher(prefixedBuffer) // Encrypt
        writeChannel.write(prefixedBuffer.array())
    }
    
    public fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    public fun handle() { // Bytes -> (Decrypt) -> (Splitter) -> (Decompress) -> (Decoder - format into packet) -> (Packet)
        val bytes = ByteArrayOutputStream(readChannel.available())
        readChannel.copyTo(bytes)
        val buffer = bytes.toByteArray().toByteBuffer()
        if (buffer.size != 0) {
            if (isEncrypted) decipher(buffer)
            // TODO find out what "TODO compression" means VVV
                // TODO compression
            if (buffer.read() == 0xfe.toByte() && buffer.read() == 0x01.toByte()) { // Check for legacy packet
                handleLegacyPacket() // Get out of here, you fool!!!
                return
            }
            buffer.reset()
            while (buffer.remaining != 0) { // Splitting and decompressing
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                if (isCompressed) decompress(payload)
                val id = payload.readVarInt() // Decoding
                val packet = netHandler.read(id, payload)
                if (Server.dispatchEvent(PacketEvent(this, packet)))
                    continue
                netHandler.process(packet)
            }
        }
    }
    
    public fun enableEncryption(encryptCipher: Cipher, decryptCipher: Cipher) {
        isEncrypted = true
        ciphers = Pair(encryptCipher, decryptCipher)
    }
    
    public fun enableCompression() {
        isCompressed = true
        send(SetCompressionPacket(Config.compressionThreshold)) // TODO: Read value from config
    }
    
    override fun toString(): String =
        "Session(ip=${socket.inetAddress.hostAddress})"
}