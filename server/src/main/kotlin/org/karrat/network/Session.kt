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
import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.packet.toBytes
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent
import org.karrat.util.readVarInt
import java.io.OutputStream
import java.net.Socket
import java.util.function.Consumer
import java.util.function.Predicate
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
            .onEach { p ->
                //Preprocessing packet - cancel packet processing if needed
                preProcess.forEach { if (it.test(p)) return@onEach }

                handler.process(p)

                //Postprocessing packet
                postProcess.forEach { it.accept(p) }
            }
            .catch { disconnect("Internal error occurred.") }
            .launchIn(ServerSocket)
    }

    companion object {
        private val preProcess = mutableListOf<Predicate<ServerboundPacket>>()
        private val postProcess = mutableListOf<Consumer<ServerboundPacket>>()

        /**
         * Adds a preProcessing callback to before handling the packet
         *
         * @param toAdd predicate to use, e.g. addPreCallback(s -> s instanceof PingPacket) would cancel PingPacket
         * processing
         */
        @JvmStatic
        fun addPreCallback(toAdd : Predicate<ServerboundPacket>) {
            preProcess.add(toAdd)
        }

        /**
         * Adds a postProcessing callback to before handling the packet
         *
         * @param toAdd predicate to use,
         */
        @JvmStatic
        fun addPostCallback(toAdd : Predicate<ServerboundPacket>) {
            preProcess.add(toAdd)
        }
    }
    
}