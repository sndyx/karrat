/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.Server
import org.karrat.server.info
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.nio
import org.karrat.struct.writeBytes

/**
 * Handles Legacy Clients by sending a kick packet, allowing customization of response
 */
internal fun Session.handleLegacyPacket() {
    val response = DynamicByteBuffer()
    response.write(0xff.toByte())
    val builder = ArrayList<String>()
    builder.add("§1") //Don't ask
    builder.add("127") // Protocol version to make it incompatible
    builder.add("1.17.1") // Minecraft server version
    builder.add(Config.legacymotd) // Legacy MOTD
    builder.add(Server.players.size.toString()) // Current player count
    builder.add(Config.maxPlayers.toString()) // Max player count
    val result = builder.joinToString("\u0000") // Delimit with null
    response.writeShort(result.length.toShort()) // Size
    response.writeBytes(result.toByteArray(Charsets.UTF_16BE)) // Stuff?
    this.socket.write(response.nio())

    info("Session $this attempted to log in with a legacy client.")
    this.socket.close()
}