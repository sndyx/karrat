/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.serialization.json.*
import org.karrat.World
import org.karrat.entity.FakePlayer
import org.karrat.entity.Player
import org.karrat.event.StatusResponseEvent
import org.karrat.struct.ByteBuffer
import org.karrat.play.ChatComponent
import org.karrat.play.Location
import org.karrat.struct.Uuid
import java.util.*

open class StatusResponse(
    var version: String,
    var protocol: Int,
    var maxPlayers: Int,
    var onlinePlayers: Int,
    var samplePlayers: List<Player>,
    var description: ChatComponent, // Edit later, accepts more primitive type of ChatComponent
    var image: ByteBuffer?
) {
    
    companion object {
        fun default() = StatusResponse( // TODO: Change to actual values later
            "Karrat 1.17.1",
            756,
            1,
            1,
            listOf(FakePlayer(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"), "sndy", Location(World(("TODO")), 0.0, 0.0, 0.0))),
            ChatComponent("Funny Gaming"),
            null
        )
    }
    
    fun compile(): JsonObject {
        
        return buildJsonObject {
            putJsonObject("version") {
                put("name", JsonPrimitive(version))
                put("protocol", JsonPrimitive(protocol))
            }
            putJsonObject("players") {
                put("max", JsonPrimitive(maxPlayers))
                put("online", JsonPrimitive(onlinePlayers))
                putJsonArray("sample") {
                    samplePlayers.forEach {
                        addJsonObject {
                            put("name", JsonPrimitive(it.name))
                            put("id", JsonPrimitive(it.uuid.toString()))
                        }
                    }
                }
            }
            putJsonObject("description") {
                put("text", JsonPrimitive(description.text))
            }
            image?.let {
                put("favicon", "data:image/png;base64,${Base64.getEncoder().encode(it.bytes)}")
            }
        }
        
    }

    override fun toString(): String =
        "StatusResponse(version=$version, protocol=$protocol, maxPlayers=$maxPlayers, onlinePlayers=$onlinePlayers, samplePlayers=$samplePlayers, description=$description)"

}
