/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

internal fun <T> Collection<T>.forEach(block: IteratorItem<T>.() -> Unit) {
    var skips: Int = 0
    forEachIndexed { index, it ->
        if (skips != 0) {
            skips--
            return@forEachIndexed
        }
        val item = IteratorItem(it, index).apply(block)
        skips += item.skips
    }
}

internal fun <T> Array<T>.forEach(block: IteratorItem<T>.() -> Unit) {
    var skips: Int = 0
    forEachIndexed { index, it ->
        if (skips != 0) {
            skips--
            return@forEachIndexed
        }
        val item = IteratorItem(it, index).apply(block)
        skips += item.skips
    }
}

internal data class IteratorItem<T>(val it: T, val index: Int) {
    
    internal var skips: Int = 0
    
    fun skip(amount: Int) {
        skips += amount
    }
    
}