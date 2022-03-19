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

public class World(
    public val identifier: Identifier,
    public val dimension: Dimension,
    public val seed: Long,
) {
    
    public val name: String
        get() = identifier.name // This is: not right

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
        blockAt(pos.posX, pos.posY, pos.posZ)

    public operator fun get(x: Int, y: Int, z: Int): Block =
        blockAt(x, y, z)

    public operator fun get(pos: BlockPos): Block =
        blockAt(pos)

    public fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        TODO()
    }

    public fun setBlock(pos: BlockPos, block: Block): Unit =
        setBlock(pos.posX, pos.posY, pos.posZ, block)

    public operator fun set(x: Int, y: Int, z: Int, block: Block): Unit =
        setBlock(x, y, z, block)

    public operator fun set(pos: BlockPos, block: Block): Unit =
        setBlock(pos, block)

}

/**
 * Fetches a [World] from its [Identifier]. Throws an Exception if not found.
 */
public fun World(identifier: Identifier): World =
    World(identifier, Dimension.Overworld, 0L)