/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.world.generation

import org.karrat.World
import org.karrat.struct.Vec2i
import org.karrat.world.Chunk

public interface WorldGenerator {
    
    public fun generateChunk(world: World, pos: Vec2i): Chunk
    
    public object Empty : WorldGenerator {
        override fun generateChunk(world: World, pos: Vec2i): Chunk = Chunk(world.height)
    }
    
}
