/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import WorldGenerator
import kotlinx.serialization.Serializable
import org.karrat.entity.Entity
import org.karrat.entity.Player
import org.karrat.internal.hash
import org.karrat.play.BlockPos
import org.karrat.play.Vec2i
import org.karrat.serialization.serializer.PrimitiveWorldSerializer
import org.karrat.struct.Identifier
import org.karrat.struct.MutableByteBuffer
import org.karrat.struct.toByteBuffer
import org.karrat.world.Block
import org.karrat.world.Chunk
import org.karrat.world.Dimension

@Serializable(with = PrimitiveWorldSerializer::class)
public class World(
    public val identifier: Identifier,
    public val dimension: Dimension,
    public val seed: Long,
    public val generator: WorldGenerator = WorldGenerator.Default,
    public val height: Int = 384
) {

    public companion object {
        public operator fun invoke(identifier: Identifier): World {
            return Server.worlds.firstOrNull { it.identifier == identifier }
                // ?: error("Unable to find world.") commented now for testing purposes
                ?: World(identifier, Dimension.Overworld, 0)
        }
    }

    public val name: String
        get() = identifier.name // This is: right :)

    public val chunks: MutableMap<Vec2i, Chunk> = mutableMapOf()
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
    
    /**
     * Gets the [Block] at position [pos].
     */
    public fun blockAt(pos: BlockPos): Block =
        get(pos.x, pos.y, pos.z)
    
    /**
     * Gets the [Block] at the given [x], [y], and [z] coordinates.
     */
    public operator fun get(x: Int, y: Int, z: Int): Block =
        get(x / 16, z / 16)[x % 16, y, z % 16]
    
    /**
     * Sets the [Block] at position [pos].
     */
    public fun setBlock(pos: BlockPos, block: Block): Unit =
        set(pos.x, pos.y, pos.z, block)
    
    /**
     * Sets the [Block] at the given [x], [y], and [z] coordinates.
     */
    public operator fun set(x: Int, y: Int, z: Int, block: Block): Unit =
        chunkAt(Vec2i(x / 16, z / 16)).set(x % 16, y, z % 16, block)
    
    /**
     * Gets the [Chunk] at the position [pos].
     */
    public fun chunkAt(pos: Vec2i): Chunk =
        chunks[pos] ?: run { generator.generateChunk(this, pos); chunks[pos]!! }
    
    /**
     * Gets the [Chunk] at the given [x] and [z] coordinates.
     */
    public operator fun get(x: Int, z: Int): Chunk = chunkAt(Vec2i(x, z))
    
}