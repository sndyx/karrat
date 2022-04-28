/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.world

import org.karrat.struct.PalettedArray

public class ChunkSection {

    public val data: PalettedArray<Block>(4096) // 16x16x16
    public var airBlocks: Int = 4096
    
    public operator fun get(x: Int, y: Int, z: Int): Block {
        data[x + y * 16 + z * 256]
    }
    
    public operator fun set(x: Int, y: Int, z: Int, value: Block) {
        // if (get(x, y, z) == Block.Air && value != Block.Air) airBlocks-- 
        // else if (value == Block.Air) airBlocks++
        // Commented until Block.kt is "done"
        data[x + y * 16 + z * 256] = value
    }
    
    public fun isEmpty(): Boolean = airBlocks == 4096
    public fun isNotEmpty(): Boolean = !isEmpty()
    
}
