/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play.clientbound

import org.karrat.World
import org.karrat.packet.ClientboundPacket
import org.karrat.play.GameMode
import org.karrat.struct.*

public class JoinGamePacket(
    public val entityId: Int,
    public val hardcore: Boolean,
    public val gamemode: GameMode,
    public val previousGamemode: GameMode, //What even is this? technically a UByte but it doesn't matter and pain
    public val worlds: List<World>,
    public val dimensionCodec : NbtCompound,
    public val dimension : NbtCompound,
    public val playerWorld: World,
    public val seed: Long,
    public val maxPlayers: Int,
    public val viewDistance: Int,
    public val reducedDebugInfo: Boolean,
) : ClientboundPacket {
    override val id: Int = 0x26;

    override fun write(data: DynamicByteBuffer) {
        data.writeVarInt(entityId)
        data.writeBoolean(hardcore)
        data.writeVarInt(worlds.size)
        worlds.forEach {
            data.writeIdentifier(it.identifier)
        }
        TODO() //NBT
        data.writeIdentifier(playerWorld.identifier)
        data.writeLong(playerWorld.seed)
        data.writeInt(maxPlayers) //Unused
        data.writeInt(viewDistance)
        data.writeBoolean(reducedDebugInfo)
        data.writeBoolean(playerWorld.debug)
        data.writeBoolean(playerWorld.flat)
    }


}