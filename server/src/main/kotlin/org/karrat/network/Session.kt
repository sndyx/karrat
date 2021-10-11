/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Server
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

    private var encryptionHandler: EncryptionHandler? = null;
    private var compressionHandler: CompressionHandler? = null;

    var packetHandler: INetHandler = NetHandlerHandshake(this)

    fun send(packet: ClientboundPacket) {
        //(Packet) -> (Encoder - format into bytes) -> (Compress) -> (Prepender) -> (Encrypt) -> Bytes

        if (dispatchEvent(PacketEvent(this, packet))) return

        //Encoding
        val buffer = DynamicByteBuffer()
        buffer.writeVarInt(packet.id)
        packet.write(buffer)
        //Compressing
        compressionHandler?.compress(buffer)
        //Prepend
        val prefixedBuffer = MutableByteBuffer(buffer.size + varSizeOf(buffer.size))
        prefixedBuffer.writePrefixed(buffer.array())
        //Encrypt
        encryptionHandler?.cipher(prefixedBuffer)
        writeChannel.write(prefixedBuffer.array())
    }
    
    fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }

    fun handle() {
        //Bytes -> (Decrypt) -> (Splitter) -> (Decompress) -> (Decoder - format into packet) -> (Packet)
        val bytes = ByteArrayOutputStream(readChannel.available())
        readChannel.copyTo(bytes)
        val buffer = bytes.toByteArray().toByteBuffer()

        if (buffer.size != 0) {
            //Decryption
            encryptionHandler?.decipher(buffer)

            //Legacy client ping
            if (buffer.read() == 0xfe.toByte() && buffer.read() == 0x01.toByte()) {
                val response = DynamicByteBuffer()
                response.write(0xff.toByte())

                val builder = ArrayList<String>()

                builder.add("127") //Protocol Version
                builder.add("1.17.1") //Minecraft Server Version
                builder.add("Legacy Client!!!") //MOTD
                builder.add("0") //Current Player Count
                builder.add("0") //Max Player Count

                //Delimit with null
                val result = builder.joinToString("\u0000")

                //size
                response.writeShort(result.length.toShort())

                //stuff
                response.writeBytes(result.toByteArray(Charsets.UTF_16BE))
                return
            }

            buffer.reset()
            while (buffer.remaining != 0) {
                //Splitting && Decompression
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                //Decoding
                compressionHandler?.decompress(payload)
                val id = payload.readVarInt()
                val packet = packetHandler.read(id, payload)
                if (dispatchEvent(PacketEvent(this, packet))) continue
                packetHandler.process(packet)
            }
        }
    }

    fun activateEncryption(encrypter: Cipher, decrypter: Cipher) {
        encryptionHandler = EncryptionHandler(encrypter, decrypter)
    }

    fun activateCompression(compressionThreshold: Int) {
        compressionHandler = CompressionHandler(compressionThreshold)
        send(SetCompressionPacket(compressionThreshold))
    }

    override fun toString(): String =
        "Session(ip=${socket.inetAddress.hostAddress})"
    
}