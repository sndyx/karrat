/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

public class PalettedList<T>(override val size: Int) : List<T> {
    
    public val palette: MutableList<T> = mutableListOf()
    public val data: VarArray<Int> = VarArray64(TODO(), TODO())
    
    override fun contains(element: T): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun containsAll(elements: Collection<T>): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun get(index: Int): T {
        TODO("Not yet implemented")
    }
    
    override fun indexOf(element: T): Int {
        TODO("Not yet implemented")
    }
    
    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
    
    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }
    
    override fun lastIndexOf(element: T): Int {
        TODO("Not yet implemented")
    }
    
    override fun listIterator(): ListIterator<T> {
        TODO("Not yet implemented")
    }
    
    override fun listIterator(index: Int): ListIterator<T> {
        TODO("Not yet implemented")
    }
    
    override fun subList(fromIndex: Int, toIndex: Int): List<T> {
        TODO("Not yet implemented")
    }
    
}