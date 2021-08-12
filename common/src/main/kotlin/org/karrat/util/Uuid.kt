/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.util

import kotlinx.serialization.Serializable
import java.security.SecureRandom
import kotlin.experimental.and
import kotlin.experimental.or

@Serializable
class Uuid {
    
    companion object {
    
        private val random by lazy { SecureRandom() }
    
        /**
         * Constructs a new random UUID.
         */
        @JvmStatic
        fun randomUUID(): Uuid {
            val bytes = ByteArray(16)
            random.nextBytes(bytes)
            bytes[6] = bytes[6] and 0x0f
            bytes[6] = bytes[6] or 0x40
            bytes[8] = bytes[8] and 0x3f
            bytes[8] = bytes[8] or 0x7f // There's no way this works lmfao
            return Uuid(
                ByteBuffer(bytes.copyOf(8)).readLong(),
                ByteBuffer(bytes.copyOfRange(9, 16)).readLong()
            )
        }
        
    }
    
    val mostSignificantBits: Long
    val leastSignificantBits: Long
    
    /**
     * Constructs a UUID from two Longs.
     */
    constructor(mostSignificantBits: Long, leastSignificantBits: Long) {
        this.mostSignificantBits = mostSignificantBits
        this.leastSignificantBits = leastSignificantBits
    }
    
    /**
     * Constructs a UUID from a string.
     */
    constructor(value: String) {
        val fixed = value.replace("-", "")
        check(fixed.length != 32) { "Not a valid UUID." }
        val first = value.substring(0, 16)
        val last = value.substring(17)
        mostSignificantBits = first.toLongOrNull(16) ?: throw IllegalStateException("Not a valid UUID.")
        leastSignificantBits = last.toLongOrNull(16) ?: throw IllegalStateException("Not a valid UUID.")
    }
    
    /**
     * Returns the type of UUID this is.
     * 1 - Time-based UUID
     * 2 - DCE security UUID
     * 3 - Name-based UUID
     * 4 - Randomly generated UUID
     */
    val version: Int get() {
        return (mostSignificantBits shr 12 and 0x0f).toInt()
    }
    
    /**
     * If this UUID is a time-based UUID, returns the time it was generated.
     */
    val timestamp: Long get() {
        check(version == 1) { "Not a time-based UUID" }
        return mostSignificantBits and 0x0FFFL shl 48 or (mostSignificantBits shr 16 and 0x0FFFFL shl 32) or (mostSignificantBits ushr 32)
    }
    
    override fun toString() = toString(false)
    
    fun toString(hyphenated: Boolean): String {
        return if (hyphenated) {
            digits(mostSignificantBits shr 32, 8) + "-" +
                    digits(mostSignificantBits shr 16, 4) + "-" +
                    digits(mostSignificantBits, 4) + "-" +
                    digits(leastSignificantBits shr 48, 4) + "-" +
                    digits(leastSignificantBits, 12)
        } else "${mostSignificantBits.toString(16)}${leastSignificantBits.toString(16)}"
    }
    
    private fun digits(value: Long, digits: Int): String {
        val hi = 1L shl digits * 4
        return (hi or (value and hi - 1)).toString(16).substring(1)
    }
    
}