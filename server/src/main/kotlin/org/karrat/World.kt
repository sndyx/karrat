/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.entity.Player
import org.karrat.internal.hash
import org.karrat.play.BlockPos
import org.karrat.struct.Identifier
import org.karrat.struct.MutableByteBuffer
import org.karrat.struct.toByteBuffer
import org.karrat.world.Block
import org.karrat.world.Chunk
import org.karrat.world.Dimension
import kotlinx.serialization.Serializable
import org.karrat.serialization.serializer.PrimitiveWorldSerializer

@Serializable(with = PrimitiveWorldSerializer::class)
public class World(
    public val identifier: Identifier,
    public val dimension: Dimension,
    public val seed: Long
) {

    public companion object {
        public operator fun invoke(identifier: Identifier): World {
            return Server.worlds.firstOrNull { it.identifier == identifier }
                ?: error("Unable to find world.")
        }
    }

    public val name: String
        get() = identifier.name // This is: right :)

    public val chunks: MutableSet<Chunk> = mutableSetOf()
    public val entities: MutableSet<Entity> = mutableSetOf()
    public val players: List<Player>
        get() = entities.filterIsInstance<Player>()
    
    /**
     * SHA-256 hashed [seed] used by the client for biome noise.
     */
    internal val hashedSeed: Long =
        hash(MutableByteBuffer(8)
            .apply { writeLong(seed) }.bytes
        ).toByteBuffer().readLong()

    // Block accessors

    public fun blockAt(x: Int, y: Int, z: Int): Block {
        TODO()
    }

    public fun blockAt(pos: BlockPos): Block =
        blockAt(pos.xPos, pos.yPos, pos.zPos)

    public operator fun get(x: Int, y: Int, z: Int): Block =
        blockAt(x, y, z)

    public operator fun get(pos: BlockPos): Block =
        blockAt(pos)

    public fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        TODO()
    }

    public fun setBlock(pos: BlockPos, block: Block): Unit =
        setBlock(pos.xPos, pos.yPos, pos.zPos, block)

    public operator fun set(x: Int, y: Int, z: Int, block: Block): Unit =
        setBlock(x, y, z, block)

    public operator fun set(pos: BlockPos, block: Block): Unit =
        setBlock(pos, block)

}