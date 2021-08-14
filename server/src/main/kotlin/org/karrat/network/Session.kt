/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import org.karrat.packet.Packet
import org.karrat.packet.play.DisconnectPacket
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent
import org.karrat.util.readVarInt
import java.io.OutputStream
import java.net.Socket
import kotlin.coroutines.coroutineContext

class Session(private val socket: Socket) {
    
    val flow = flow {
        while (coroutineContext.isActive) {
            val buffer = ByteBuffer(readChannel.readBytes())
            if (buffer.size != 0) {
                val length = buffer.readVarInt()
                val payload = buffer.readStream(length)
                val id = payload.readVarInt()
                val packet = packetAdapter.read(id, payload)
                emit(packet)
            } else {
                if (!isAlive) break
                delay(10)
            }
        }
    }
    
    val isAlive get() = !socket.isClosed
    private val readChannel by lazy { socket.getInputStream().buffered() }
    private val writeChannel: OutputStream by lazy { socket.getOutputStream() }
    val packetAdapter: IPacketAdapter = PacketAdapterHandshake()
    
    fun send(packet: Packet) = writeChannel.write(packet.toBytes())
    
    fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    init {
        flow
            .onEach { }
            .catch { disconnect("Internal error occurred.") }
            .launchIn(ServerSocket)
    }
    
}