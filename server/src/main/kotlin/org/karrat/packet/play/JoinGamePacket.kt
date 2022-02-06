/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play

import org.karrat.World
import org.karrat.packet.ClientboundPacket
import org.karrat.play.GameMode
import org.karrat.struct.*

public class JoinGamePacket(
    public val entityId: Int,
    public val isHardcore: Boolean,
    public val gameMode: GameMode,
    public val previousGameMode: GameMode,
    public val worlds: List<World>,
    public val dimensionCodec: NbtCompound,
    public val dimension: NbtCompound,
    public val world: World,
    // public val maxPlayers: Int, **Unused by minecraft, ignore.**
    public val viewDistance: Int,
    public val simulationDistance: Int,
    public val reducedDebugInfo: Boolean,
    public val enableRespawnScreen: Boolean,
    public val isDebug: Boolean,
    public val isFlat: Boolean
) : ClientboundPacket {
    override val id: Int = 0x26

    override fun write(data: DynamicByteBuffer): Unit = data.run {
        writeVarInt(entityId)
        writeBoolean(isHardcore)
        writeUByte(gameMode.id.toUByte())
        write(previousGameMode.id.toByte())
        writeVarInt(worlds.size)
        worlds.forEach {
            data.writeIdentifier(it.identifier)
        }
        writeNbt(dimensionCodec)
        writeNbt(dimension)
        writeIdentifier(world.identifier)
        writeLong(world.hashedSeed)
        writeVarInt(0) //Unused
        writeVarInt(viewDistance)
        writeVarInt(simulationDistance)
        writeBoolean(reducedDebugInfo)
        writeBoolean(enableRespawnScreen)
        data.writeBoolean(isDebug)
        data.writeBoolean(isFlat)
    }


}