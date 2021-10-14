/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet.play.clientbound

import org.karrat.play.GameMode
import org.karrat.struct.Identifier
import org.karrat.struct.NbtCompound

public class JoinGamePacket(
    public val id: Int,
    public val hardcore: Boolean,
    public val gamemode: GameMode,
    public val previousGamemode: GameMode, //What even is this?
    public val worldNames: List<Identifier>,
    public val dimensionCodec : NbtCompound,
    public val dimension : NbtCompound,
    public val worldName: Identifier,
    public val seed: Long,
    public val maxPlayers: Int,
    public val viewDistance: Int,
    public val reducedDebugInfo: Boolean,
    public val isDebugWorld: Boolean,
    public val isFlat: Boolean
) {
}