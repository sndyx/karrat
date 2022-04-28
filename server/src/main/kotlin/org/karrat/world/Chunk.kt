/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.world

import kotlin.math.mod

public class Chunk(minY: Int, height: Int) {
    
    public val sections: List<ChunkSection>(height / 16) { ChunkSection() }
    
    public operator fun get(x: Int, y: Int, z: Int): Block =
        sections[y / 16][x, y mod 16, z]
    
    public operator fun set(x: Int, y: Int, z: Int, value: Block) {
        sections[y / 16][x, y mod 16, z] = value  
    }

}
