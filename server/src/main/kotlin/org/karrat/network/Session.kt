/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.ServerConfigs
import org.karrat.entity.Player
import org.karrat.event.PacketEvent
import org.karrat.event.dispatchEvent
import org.karrat.packet.ClientboundPacket
import org.karrat.packet.login.clientbound.SetCompressionPacket
import org.karrat.packet.play.DisconnectPacket
import org.karrat.play.ChatComponent
import org.karrat.struct.*
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.Socket
import javax.crypto.Cipher

class Session(val socket: Socket) {
    
    private val player: Player? = null
    
    val isAlive get() = !socket.isClosed
    
    private val readChannel = socket.getInputStream().buffered()
    private val writeChannel: OutputStream by lazy { socket.getOutputStream() }
    
    private var encryptionHandler: EncryptionHandler? = null
    var compression = false
    
    var netHandler: NetHandler = NetHandlerHandshake(this)
    
    fun send(packet: ClientboundPacket) { // (Packet) -> (Encoder - format into bytes) -> (Compress) -> (Prepended) -> (Encrypt) -> Bytes
        if (dispatchEvent(PacketEvent(this, packet))) return
        val buffer = DynamicByteBuffer()
        buffer.writeVarInt(packet.id)
        packet.write(buffer)
        if (compression) compress(buffer)
        val prefixedBuffer = MutableByteBuffer(buffer.size + varSizeOf(buffer.size)) // Prepend
        prefixedBuffer.writePrefixed(buffer.array())
        encryptionHandler?.cipher(prefixedBuffer) // Encrypt
        writeChannel.write(prefixedBuffer.array())
    }
    
    fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    fun handle() { // Bytes -> (Decrypt) -> (Splitter) -> (Decompress) -> (Decoder - format into packet) -> (Packet)
        val bytes = ByteArrayOutputStream(readChannel.available())
        readChannel.copyTo(bytes)
        val buffer = bytes.toByteArray().toByteBuffer()
        if (buffer.size != 0) {
            encryptionHandler?.decipher(buffer)
            // TODO compression
            if (buffer.read() == 0xfe.toByte() && buffer.read() == 0x01.toByte()) { // Check for legacy packet
                handleLegacyPacket()
                return
            }
            buffer.reset()
            while (buffer.remaining != 0) { // Splitting and decompressing
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                if (compression) decompress(payload)
                val id = payload.readVarInt() // Decoding
                val packet = netHandler.read(id, payload)
                if (dispatchEvent(PacketEvent(this, packet))) continue
                netHandler.process(packet)
            }
        }
    }
    
    fun enableEncryption(encrypter: Cipher, decrypter: Cipher) {
        encryptionHandler = EncryptionHandler(encrypter, decrypter)
    }
    
    fun enableCompression() {
        compression = true
        send(SetCompressionPacket(ServerConfigs.network_compression_threshold)) // TODO: Read value from config
    }
    
    override fun toString(): String =
        "Session(ip=${socket.inetAddress.hostAddress})"
}