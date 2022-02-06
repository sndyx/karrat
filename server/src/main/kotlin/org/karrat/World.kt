/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.entity.Player
import org.karrat.internal.hash
import org.karrat.struct.Identifier
import org.karrat.struct.MutableByteBuffer
import org.karrat.struct.toByteBuffer
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
    public val hashedSeed: Long =
        hash(MutableByteBuffer(8)
            .apply { writeLong(seed) }.bytes
        ).toByteBuffer().readLong()

}

/**
 * Fetches a [World] from its [Identifier]. Throws an Exception if not found.
 */
public fun World(identifier: Identifier): World =
    World(identifier, Dimension.Overworld, 0L)