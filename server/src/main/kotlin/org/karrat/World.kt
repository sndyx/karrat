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
import kotlinx.serialization.Transient

@Serializable
public class World internal constructor(public val identifier: Identifier) {

    public constructor (
        identifier: Identifier,
        dimension: Dimension,
        seed: Long
    ) : this(identifier) {
        // This is a mess
        // TODO: FIX THIS SHIT
    }

    @Transient
    public val dimension: Dimension = Dimension.Overworld
    @Transient
    public val seed: Long = 0

    public val name: String
        get() = identifier.name // This is: not right

    @Transient
    public val chunks: MutableSet<Chunk> = mutableSetOf()
    @Transient
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