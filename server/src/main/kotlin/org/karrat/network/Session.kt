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
import org.karrat.packet.clientbound.ClientboundPacket
import org.karrat.packet.clientbound.play.DisconnectPacket
import org.karrat.packet.toBytes
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent
import org.karrat.util.readVarInt
import java.io.OutputStream
import java.net.Socket
import kotlin.coroutines.coroutineContext

class Session(private val socket: Socket) {
    
    private val flow = flow {
        while (coroutineContext.isActive) {
            val buffer = ByteBuffer(readChannel.readBytes())
            if (buffer.size != 0) {
                val length = buffer.readVarInt()
                val payload = buffer.readStream(length)
                val id = payload.readVarInt()
                val packet = handler.read(id, payload)
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
    
    var handler: INetHandler = NetHandlerHandshake(this)
    
    fun send(packet: ClientboundPacket) = writeChannel.write(packet.toBytes())
    
    fun disconnect(reason: String) {
        send(DisconnectPacket(ChatComponent(reason)))
        socket.close()
    }
    
    init {
        flow
            .onEach { it.process(handler) }
            .catch { disconnect("Internal error occurred.") }
            .launchIn(ServerSocket)
    }
    
}