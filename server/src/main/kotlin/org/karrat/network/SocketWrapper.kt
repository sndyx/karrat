/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.internal.NioByteBuffer
import org.karrat.internal.NioSocketChannel
import org.karrat.struct.ByteBuffer
import org.karrat.struct.array
import org.karrat.struct.toByteBuffer
import java.io.Closeable
import java.net.SocketAddress

/*
    A wrapper for the NioSocketChannel with relevant reading and write tools
 */
public class SocketWrapper(private val socket: NioSocketChannel) : Closeable {
    
    public val remoteAddress: SocketAddress get() = socket.remoteAddress
    public val isOpen: Boolean get() = socket.isOpen
    
    public fun read(): ByteBuffer {
        val buffer = NioByteBuffer.allocate(1028)
        /*
            This probably blocks since the socket is blocking
            Since this code is in a separate launch thread, meaning that if the client does not respond
            A lot of threads would build up and lag the server eventually

            A fix would probably be something that involves coroutines or threads, or separate handling for
            each player :(
         */
        socket.read(buffer)
        return buffer
            .array()
            .copyOf(1028 - buffer.remaining())
            .toByteBuffer()
    }
    
    public fun write(src: ByteBuffer) {
        val buffer = NioByteBuffer.allocate(src.size)
        buffer.put(src.array())
        buffer.flip()
        socket.write(buffer)
    }
    
    public override fun close() {
        socket.close()
    }

}
