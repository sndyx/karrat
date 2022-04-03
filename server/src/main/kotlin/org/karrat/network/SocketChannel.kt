/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network

import org.karrat.internal.NioByteBuffer
import org.karrat.internal.NioSocketChannel
import org.karrat.struct.ByteBuffer
import org.karrat.struct.array
import org.karrat.struct.byteBufferOf
import org.karrat.struct.toByteBuffer
import java.io.Closeable
import java.net.SocketAddress

public open class SocketChannel(private val socket: NioSocketChannel) : Closeable {

    public val buffer: NioByteBuffer = NioByteBuffer.allocate(Config.networkBufferSize)
    public val remoteAddress: SocketAddress get() = socket.remoteAddress
    public val isOpen: Boolean get() = socket.isOpen

    public open fun read(): ByteBuffer {
        socket.read(buffer)
        return buffer
            .array()
            .copyOf(Config.networkBufferSize - buffer.remaining())
            .toByteBuffer()
            .also {
                buffer.clear() // Reset buffer for reading
            }
    }

    public open fun write(src: ByteBuffer) {
        val buffer = NioByteBuffer.allocate(src.size)
        buffer.put(src.array())
        buffer.flip()
        socket.write(buffer)
    }

    public override fun close() {
        socket.close()
    }

    public object Dummy : SocketChannel(NioSocketChannel.open()) {

        override fun read(): ByteBuffer {
            return byteBufferOf()
        }

        override fun write(src: ByteBuffer) { /* Ignore */ }

    }

}
