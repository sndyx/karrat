/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.karrat.entity.Player
import org.karrat.struct.ByteBuffer
import org.karrat.play.ChatComponent
import java.util.stream.Collectors

data class StatusResponse(
    var version: String,
    var protocol: Int,
    var maxPlayers: Int,
    var onlinePlayers: Int,
    var samplePlayers: List<Player>,
    var description: ChatComponent,
    var image: ByteBuffer?
) {

    override fun toString(): String {
        return JsonObject(
            mapOf(
                "version" to JsonObject(
                    mapOf(
                        "name" to JsonPrimitive(version),
                        "protocol" to JsonPrimitive(protocol),
                    )
                ),
                "players" to JsonObject(
                    mapOf(
                        "max" to JsonPrimitive(maxPlayers),
                        "online" to JsonPrimitive(onlinePlayers),
                        "sample" to JsonArray(
                            samplePlayers.stream()
                                .map { p -> JsonObject(mapOf("name" to JsonPrimitive(p.name), "id" to JsonPrimitive(p.uuid.toString()))) }
                                .collect(Collectors.toList())
                        )
                    )
                ),
                "description" to JsonObject(
                    mapOf(
                        "text" to JsonPrimitive(description.text),
                    )
                )
            )
        ).toString()
    }

}
