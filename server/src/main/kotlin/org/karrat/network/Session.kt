/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.entity.Player
import org.karrat.event.PacketEvent
import org.karrat.event.dispatchEvent
import org.karrat.struct.readBuffer
import org.karrat.struct.readVarInt
import org.karrat.struct.toByteBuffer
import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.packet.clientbound.play.DisconnectPacket
import org.karrat.packet.toBytes
import org.karrat.play.ChatComponent
import org.karrat.server.info
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.Socket

class Session(val socket: Socket) {
    
    private val player: Player? = null
    
    val isAlive get() = !socket.isClosed
    
    private val readChannel = socket.getInputStream().buffered()
    private val writeChannel: OutputStream by lazy { socket.getOutputStream() }
    
    var handler: INetHandler = NetHandlerHandshake(this)
    
    fun send(packet: ClientboundPacket) {
        writeChannel.write(packet.toBytes())
        dispatchEvent(PacketEvent(this, packet))
    }
    
    fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }

    fun handle() {
        val bytes = ByteArrayOutputStream(readChannel.available())
        readChannel.copyTo(bytes)
        val buffer = bytes.toByteArray().toByteBuffer()
        if (buffer.size != 0) {
            if (buffer.read() == 0xfe.toByte() && buffer.read() == 0x01.toByte()) return
            buffer.reset()
            while (buffer.remaining != 0) {
                val length = buffer.readVarInt()
                val payload = buffer.readBuffer(length)
                val id = payload.readVarInt()
                val packet = handler.read(id, payload)
                if (dispatchEvent(PacketEvent(this, packet))) continue
                handler.process(packet)
            }
        }
    }
    
    override fun toString(): String =
        "Session(ip=${socket.inetAddress.hostAddress})"
    
}