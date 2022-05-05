/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.handler

import org.karrat.Server
import org.karrat.network.NetHandler
import org.karrat.network.Session
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.play.JoinGamePacket
import org.karrat.play.GameMode
import org.karrat.struct.ByteBuffer

public class NetHandlerPlay(private val session: Session) : NetHandler {

    init {
        /*
        session.send(JoinGamePacket(
            entityId = 0,
            isHardcore = false,
            gameMode = GameMode.Survival,
            previousGameMode = GameMode.Survival,
            worlds = Server.worlds,
            dimensionCodec =
        ))
        /*
        S → C: Join Game
        S → C: Plugin Message: minecraft:brand with the server's brand (Optional)
        S → C: Server Difficulty (Optional)
        S → C: Player Abilities (Optional)
        C → S: Plugin Message: minecraft:brand with the client's brand (Optional)
        C → S: Client Settings
        S → C: Held Item Change
        S → C: Declare Recipes
        S → C: Tags
        S → C: Entity Status (for the OP permission level; see Entity statuses#Player)
        S → C: Declare Commands
        S → C: Unlock Recipes
        S → C: Player Position And Look
        S → C: Player Info (Add Player action)
        S → C: Player Info (Update latency action)
        S → C: Update View Position
        S → C: Update Light (One sent for each chunk in a square centered on the player's position)
        S → C: Chunk Data (One sent for each chunk in a square centered on the player's position)
        S → C: World Border (Once the world is finished loading)
        S → C: Spawn Position (“home” spawn, not where the client will spawn on login)
        S → C: Player Position And Look (Required, tells the client they're ready to spawn)
        C → S: Teleport Confirm
        C → S: Player Position And Look (to confirm the spawn position)
        C → S: Client Status (sent either before or while receiving chunks, further testing needed, server handles correctly if not sent)
        S → C: inventory, entities, etc
         */*/
    }

    override fun read(id: Int, data: ByteBuffer): ServerboundPacket = when(id) {
        0x26 -> TODO()
        else -> error("Failed")
    }

    override fun process(packet: ServerboundPacket) {
        TODO("Not yet implemented")
    }

}