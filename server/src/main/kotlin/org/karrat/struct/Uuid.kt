/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable
import org.karrat.server.fatal
import java.security.SecureRandom
import kotlin.experimental.and
import kotlin.experimental.or

@Serializable
class Uuid {
    
    val mostSignificantBits: Long
    val leastSignificantBits: Long
    
    /**
     * Constructs a Uuid from two Longs.
     */
    constructor (mostSignificantBits: Long, leastSignificantBits: Long) {
        this.mostSignificantBits = mostSignificantBits
        this.leastSignificantBits = leastSignificantBits
    }
    
    /**
     * Reads a Uuid from a string.
     */
    constructor(value: String) {
        val fixed = value.replace("-", "")
        check(fixed.length != 32) { "Not a valid Uuid." }
        val first = value.substring(0, 16)
        val last = value.substring(17)
        mostSignificantBits = first.toLongOrNull(16) ?: throw IllegalStateException("Not a valid Uuid.")
        leastSignificantBits = last.toLongOrNull(16) ?: throw IllegalStateException("Not a valid Uuid.")
    }
    
    companion object {
    
        private val random by lazy { SecureRandom() }
    
        fun random(): Uuid {
            val bytes = ByteArray(16)
            random.nextBytes(bytes)
            bytes[6] = bytes[6] and 0x0f
            bytes[6] = bytes[6] or 0x40
            bytes[8] = bytes[8] and 0x3f
            bytes[8] = bytes[8] or 0x80.toByte() // frick you kotlin!! i dont play by the rules
            return Uuid(
                ByteBuffer(bytes.copyOf(8)).readLong(),
                ByteBuffer(bytes.copyOfRange(9, 16)).readLong()
            )
        }
        
    }
    
    /**
     * Represents the type of Uuid this is.
     * 1 - Time-based Uuid
     * 2 - DCE security Uuid
     * 3 - Name-based Uuid
     * 4 - Randomly generated Uuid
     */
    val version: Int get() {
        return (mostSignificantBits shr 12 and 0x0f).toInt()
    }
    
    /**
     * If this Uuid is a time-based Uuid, returns the time it was generated.
     */
    val timestamp: Long get() {
        check(version == 1) { fatal("Uuid is not a time-based Uuid. See Uuid::version.") }
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