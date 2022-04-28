/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

/**
 * An array of variable-length numbers.
 */
public interface VarArray<T : Number> {
    
    public var blockSize: Int
    
    public operator fun get(index: Int): T
    
    public operator fun set(index: Int, value: T)
    
}

/**
 * A low-memory-impact [VarArray] implementation where data is stored in 64-bit
 * chunks.
 */
public class VarArray64<T : Number>(public val size: Int, bitsPerValue: Int) : VarArray<T> {
    
    public val chunkCapacity: Int = 64 / blockSize
    public val chunks: MutableList<Chunk> = MutableList((size - 1) / chunkCapacity + 1) { Chunk() }
    
    private var _blockSize: Int = bitsPerValue
    override var blockSize: Int
        get() = _blockSize
        set(value) {
            _blockSize = value
            resize()
        }
    
    override operator fun get(index: Int): T =
        chunks[index / chunkCapacity][index.mod(chunkCapacity)]
    
    override operator fun set(index: Int, value: T) {
        chunks[index / chunkCapacity][index.mod(chunkCapacity)] = value
    }
    
    private fun resize() {
        TODO("This is hard :( ...")
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