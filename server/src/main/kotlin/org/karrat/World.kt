/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.entity.Entity
import org.karrat.entity.Player

class World(
    val name: String,
    val dimension: Any
) {
    
    val entities = emptyList<Entity>()
    
}

fun World(name: String) = World(name, "TODO")