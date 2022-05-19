/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.world

public class Chunk(height: Int) : Iterable<Block> {
    
    public val sections: List<ChunkSection> = List(height / 16) { ChunkSection() }
    
    public operator fun get(x: Int, y: Int, z: Int): Block =
        sections[y / 16][x, y % 16, z]
    
    public operator fun set(x: Int, y: Int, z: Int, value: Block) {
        sections[y / 16][x, y % 16, z] = value
    }

    override fun iterator(): Iterator<Block> =
        iterator {
            sections.forEach {
                yieldAll(it.iterator())
            }
        }

}
