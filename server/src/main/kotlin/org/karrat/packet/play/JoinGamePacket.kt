/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play

import org.karrat.World
import org.karrat.packet.ClientboundPacket
import org.karrat.world.dimensions.GameMode
import org.karrat.struct.*

public class JoinGamePacket(
    public val entityId: Int,
    public val hardcore: Boolean,
    public val gamemode: GameMode,
    public val previousGamemode: GameMode, //What even is this? technically a UByte but it doesn't matter and pain
    public val worlds: List<World>,
    public val dimensionCodec: NbtCompound,
    public val dimension: NbtCompound,
    public val playerWorld: World,
    public val seed: Long,
    //public val maxPlayers: Int,
    public val viewDistance: Int,
    public val reducedDebugInfo: Boolean,
) : ClientboundPacket {
    override val id: Int = 0x26

    override fun write(data: DynamicByteBuffer) {
        data.writeVarInt(entityId)
        data.writeBoolean(hardcore)
        data.write(gamemode.toId()) //technically a ubyte
        data.write(previousGamemode.toId())
        data.writeVarInt(worlds.size)
        worlds.forEach {
            data.writeIdentifier(it.identifier)
        }
        //data.writeVarInt()
        TODO() //NBT
        data.writeIdentifier(playerWorld.identifier)
        data.writeLong(playerWorld.seed)
        data.writeInt(0) //Unused
        data.writeInt(viewDistance)
        data.writeBoolean(reducedDebugInfo)
        //data.writeBoolean(playerWorld.debug)
        //data.writeBoolean(playerWorld.flat) later
    }


}