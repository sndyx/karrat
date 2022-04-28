/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

/**
 * An array of variable-length numbers.
 */
public interface VarArray<T : Number> {
    
    public operator fun get(index: Int): T
    
    public operator fun set(index: Int, value: T)
    
}

/**
 * A low-memory-impact [VarArray] implementation where data is stored in 64-bit
 * chunks.
 */
public class VarArray64<T : Number>(public val size: Int, public val blockSize: Int) : VarArray<T> {
    
    public val chunkCapacity: Int = 64 / blockSize
    public val chunks: List<Chunk> = List((size + chunkCapacity - 1) / chunkCapacity) { Chunk() }
    
    override operator fun get(index: Int): T =
        chunks[(index + chunkCapacity) / chunkCapacity - 1][index.mod(chunkCapacity)]
    
    override operator fun set(index: Int, value: T) {
        chunks[(index + chunkCapacity) / chunkCapacity - 1][index.mod(chunkCapacity)] = value
    }
    
    public inner class Chunk {
        
        public val data: Long = 0L
        
        @Suppress("Unchecked_Cast")
        public operator fun get(index: Int): T =
            (((1 shl blockSize) - 1) and ((data shr ((index * blockSize) - 1)).toInt())) as T
        
        public operator fun set(index: Int, value: T) {
            TODO("stupid bit logic")
        }
        
    }
    
}