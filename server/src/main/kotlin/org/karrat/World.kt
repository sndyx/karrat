/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity

public class World(
    public val name: String,
    public val dimension: Any
) {
    
    public val entities: MutableList<Entity> = mutableListOf()
    
}

public fun World(name: String): World = World(name, "TODO")