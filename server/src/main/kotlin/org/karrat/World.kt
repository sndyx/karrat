/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.struct.Identifier

public class World(
    public val identifier: Identifier,
    public val dimension: Any
) {

    public val name: String
    get() = identifier.id
    
    public val entities: MutableList<Entity> = mutableListOf()

    public var seed: Long = 0L
    public var debug: Boolean = false
    public var flat: Boolean = false
}

public fun World(identifier: Identifier): World = World(identifier, "TODO")