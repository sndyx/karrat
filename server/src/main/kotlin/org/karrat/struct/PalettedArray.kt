/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

public class PalettedArray<T>(public val size: Int) : Iterable<T> {
    
    public val palette: Palette = Palette()
    public val data: VarArray<Int> = VarArray64(size, 0)
    
    public operator fun get(index: Int): T = palette[data[index]]
    
    public operator fun set(index: Int, value: T) {
        if (!palette.contains(value)) {
            palette.add(value)
        }
        data[index] = palette.indexOf(value)
    }
    
    public inner class Palette {
    
        private val set = mutableSetOf<T>()
        public val size: Int get() = set.size
        public val bits: Int get() = 32 - set.size.countLeadingZeroBits()
        
        public operator fun get(index: Int): T = set.elementAt(index)
        
        public fun add(element: T): Boolean {
            if (32 - (set.size + 1).countLeadingZeroBits() > bits) data.blockSize++
            return set.add(element)
        }
    
        public fun remove(element: T): Boolean {
            if (32 - (set.size - 1).countLeadingZeroBits() < bits) data.blockSize--
            return set.remove(element)
        }
    
        public fun contains(element: T): Boolean = set.contains(element)
        
        public fun indexOf(element: T): Int = set.indexOf(element)
    
    }

    override fun iterator(): Iterator<T> =
        iterator {
            repeat(size) {
                yield(get(it))
            }
        }

}