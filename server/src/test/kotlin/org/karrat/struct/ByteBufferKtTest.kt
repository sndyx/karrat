/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ByteBufferKtTest {
    
    @Test
    fun readBytes() {
        val buffer = byteBufferOf(15, 39, 20, 19, 12, 19)
        val bytes = buffer.readBytes()
        assertArrayEquals(byteArrayOf(15, 39, 20, 19, 12, 19), bytes)
    }
    
}