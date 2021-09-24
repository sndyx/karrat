/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.entity.Player
import org.karrat.util.ByteBuffer
import org.karrat.util.ChatComponent

data class StatusResponse(
    var version: String,
    var protocol: Int,
    var maxPlayers: Int,
    var onlinePlayers: Int,
    var samplePlayers: List<Player>,
    var description: ChatComponent,
    var image: ByteBuffer?
) {
  
    override fun toString(): String =
        "StatusResponse(version=$version, protocol=$protocol, maxPlayers=$maxPlayers, onlinePlayers=$onlinePlayers, samplePlayers=$samplePlayers, description=$description)"
    
}
