/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.struct.Identifier
import org.karrat.world.Chunk
import org.karrat.world.WorldData

public class World(
    public val identifier: Identifier,
    public val dimension: Any,
    public val seed: Long,
) {

    public val name: String
        get() = identifier.name
    
    public val chunks: MutableSet<Chunk> = mutableSetOf()
    public val entities: MutableSet<Entity> = mutableSetOf()
    public val data: WorldData = WorldData()
    
}

/**
 * Fetches a [World] from its [Identifier]. Throws an Exception if not found.
 */
public fun World(identifier: Identifier): World = World(identifier, "TODO", 0L)