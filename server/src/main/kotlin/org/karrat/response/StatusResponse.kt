/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.response

import kotlinx.serialization.json.*
import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.DummyPlayer
import org.karrat.entity.Player
import org.karrat.struct.TextComponent
import org.karrat.struct.Uuid
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readBytes

/**
 * Represents the data sent to display a server banner on a client's server list
 * gui. Includes information such as the server [version], [image], and
 * [description].
 */
public class StatusResponse(
    public var version: String,
    public var protocol: Int,
    public var maxPlayers: Int,
    public var onlinePlayers: Int,
    public var samplePlayers: List<Player>,
    public var description: TextComponent, // Edit later, accepts more primitive type of ChatComponent
    public var image: ByteArray? = null
) {

    public companion object {

        public fun default(): StatusResponse = StatusResponse( // TODO: Change to actual values later
            Config.versionName,
            756,
            1,
            Server.players.size,
            listOf(DummyPlayer(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"), "sndy")),
            TextComponent(Config.motd),
            Path("icon.jpeg").takeIf { it.exists() }?.readBytes()
                ?: StatusResponse::class.java.getResource("/defaults/icon.jpeg")?.readBytes()
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
