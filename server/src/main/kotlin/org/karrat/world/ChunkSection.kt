/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.world

import org.karrat.struct.PalettedArray

public class ChunkSection : Iterable<Block> {

    public val data: PalettedArray<Block> = PalettedArray(4096) // 16x16x16
    public var airBlocks: Int = 4096
    
    public operator fun get(x: Int, y: Int, z: Int): Block =
        data[x + y * 16 + z * 256]
    
    public operator fun set(x: Int, y: Int, z: Int, value: Block) {
        if (get(x, y, z) is Block.Air && value !is Block.Air) airBlocks--
        else if (value is Block.Air) airBlocks++
        data[x + y * 16 + z * 256] = value
    }
    
    public fun isEmpty(): Boolean = airBlocks == 4096
    public fun isNotEmpty(): Boolean = !isEmpty()

    override fun iterator(): Iterator<Block> = data.iterator()

}
