/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.server.info
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.array
import org.karrat.struct.nio
import org.karrat.struct.writeBytes

internal fun Session.handleLegacyPacket() {
    val response = DynamicByteBuffer()
    response.write(0xff.toByte())
    val builder = ArrayList<String>()
    builder.add("§1") //Don't ask
    builder.add("127") // Protocol version
    builder.add("1.17.1") // Minecraft server version
    builder.add(Config.legacymotd) // MOTD
    builder.add("0") // Current player count
    builder.add("0") // Max player count
    val result = builder.joinToString("\u0000") // Delimit with null
    response.writeShort(result.length.toShort()) // Size
    response.writeBytes(result.toByteArray(Charsets.UTF_16BE)) // Stuff?
    this.socket.write(response.nio())

    info("Session $this attempted to log in with a legacy client.")
    this.socket.close()
}