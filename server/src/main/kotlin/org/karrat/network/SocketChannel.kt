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

public class SocketChannel(private val socket: NioSocketChannel) : Closeable {

    public val remoteAddress: SocketAddress get() = socket.remoteAddress
    public val isOpen: Boolean get() = socket.isOpen

    public fun read(): ByteBuffer {
        val buffer = NioByteBuffer.allocate(1024)
        socket.read(buffer)
        return buffer
            .array()
            .copyOf(1024 - buffer.remaining())
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
