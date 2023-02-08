/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.struct

/**
 * An array of variable-length numbers.
 */
public interface VarArray<T : Number> : Iterable<T> {

    public val size: Int
    public var blockSize: Int
    
    public operator fun get(index: Int): T
    
    public operator fun set(index: Int, value: T)

    override fun iterator(): Iterator<T> =
        iterator {
            repeat(size) {
                yield(get(it))
            }
        }

}

/**
 * A low-memory-impact [VarArray] implementation where data is stored in 64-bit
 * chunks.
 */
public class VarArray64<T : Number>(override val size: Int, bitsPerValue: Int) : VarArray<T> {
    
    public val chunkCapacity: Int get() = 64 / blockSize
    public val chunks: MutableList<Chunk> = MutableList((size - 1) / chunkCapacity + 1) { Chunk() }
    
    private var _blockSize: Int = bitsPerValue
    override var blockSize: Int
        get() = _blockSize
        set(value) {
            resize(value)
        }
    
    override operator fun get(index: Int): T =
        chunks[index / chunkCapacity][index.mod(chunkCapacity)]

    override operator fun set(index: Int, value: T) {
        chunks[index / chunkCapacity][index.mod(chunkCapacity)] = value
    }
    
    private fun resize(newBlockSize: Int) {
        val newChunkCapacity: Int = 64 / newBlockSize
        if (newBlockSize > blockSize) {
            chunks.addAll(Array((size - 1) / newChunkCapacity - (size - 1) / chunkCapacity) { Chunk() })
        }
        val oldSize = _blockSize
        for (index in if (newBlockSize > blockSize) (size - 1) downTo 0 else 0 until size) {
            val data = get(index)
            _blockSize = newBlockSize
            chunks[index / newChunkCapacity][index.mod(newChunkCapacity)] = data
            _blockSize = oldSize
        }
        if (newBlockSize < blockSize) {
            repeat((size - 1) / chunkCapacity - (size - 1) / newChunkCapacity) { chunks.removeLast() }
        }
        _blockSize = newBlockSize
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