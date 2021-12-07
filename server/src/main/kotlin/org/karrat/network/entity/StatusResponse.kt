/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network.entity

import kotlinx.serialization.json.*
import org.karrat.Config
import org.karrat.World
import org.karrat.entity.FakePlayer
import org.karrat.entity.Player
import org.karrat.play.ChatComponent
import org.karrat.play.Location
import org.karrat.server.info
import org.karrat.struct.Uuid
import java.io.File
import java.util.*
import kotlin.io.path.Path

public open class StatusResponse(
    public var version: String,
    public var protocol: Int,
    public var maxPlayers: Int,
    public var onlinePlayers: Int,
    public var samplePlayers: List<Player>,
    public var description: ChatComponent, // Edit later, accepts more primitive type of ChatComponent
    public var image: ByteArray? = null
) {
    
    public companion object {
        
        public fun default(): StatusResponse = StatusResponse( // TODO: Change to actual values later
            "Karrat 1.17.1",
            756,
            1,
            1000000000,
            listOf(FakePlayer(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"), "sndy")),
            ChatComponent(Config.motd),
            StatusResponse::class.java.getResource("/icon.png")?.readBytes()
        )
        
    }
    
    public fun compile(): JsonObject = buildJsonObject {
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
            put("favicon", "data:image/png;base64,${Base64.getEncoder().encodeToString(it)}")
        }
    }

    override fun toString(): String =
        "StatusResponse(version=$version, protocol=$protocol," +
                " maxPlayers=$maxPlayers, onlinePlayers=$onlinePlayers," +
                " samplePlayers=$samplePlayers, description=$description)"

}
