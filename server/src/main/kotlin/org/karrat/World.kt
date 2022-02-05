/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.entity.Player
import org.karrat.struct.Identifier
import org.karrat.world.Chunk
import org.karrat.world.Dimension
import org.karrat.world.WorldData

public class World(
    public val identifier: Identifier,
    public val dimension: Dimension,
    public val seed: Long,
) {

    public val name: String
        get() = identifier.name

    public val chunks: MutableSet<Chunk> = mutableSetOf()
    public val entities: MutableSet<Entity> = mutableSetOf()
    public val players: List<Player>
        get() = entities.filterIsInstance<Player>()

    public val data: WorldData = WorldData()

}

/**
 * Fetches a [World] from its [Identifier]. Throws an Exception if not found.
 */
public fun World(identifier: Identifier): World = World(identifier, Dimension.Overworld, 0L)