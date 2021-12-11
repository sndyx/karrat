/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.karrat.server.fatal
import java.security.SecureRandom
import kotlin.experimental.and
import kotlin.experimental.or

@Serializable
public class Uuid {

    public val mostSignificantBits: Long
    public val leastSignificantBits: Long

    /**
     * Constructs a Uuid from two Longs.
     */
    public constructor (mostSignificantBits: Long, leastSignificantBits: Long) {
        this.mostSignificantBits = mostSignificantBits
        this.leastSignificantBits = leastSignificantBits
    }

    /**
     * Reads a Uuid from a string.
     */
    public constructor (value: String) {
        val fixed = value.replace("-", "")
        check(fixed.length == 32) { fatal("Not a valid Uuid.") }
        val buffer = ByteBuffer(fixed.decodeHex())
        mostSignificantBits = buffer.readLong()
        leastSignificantBits = buffer.readLong()
    }

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }

    public companion object {

        private val random by lazy { SecureRandom() }

        public fun random(): Uuid {
            val bytes = ByteArray(16)
            random.nextBytes(bytes)
            bytes[6] = bytes[6] and 0x0f
            bytes[6] = bytes[6] or 0x40
            bytes[8] = bytes[8] and 0x3f
            bytes[8] = bytes[8] or -0x80 //.toByte() // frick you kotlin!! i dont play by the rules

            val buffer = ByteBuffer(bytes)

            return Uuid(
                buffer.readLong(),
                buffer.readLong()
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
    public val version: Int
        get() {
            return (mostSignificantBits shr 12 and 0x0f).toInt()
        }

    /**
     * If this Uuid is a time-based Uuid, returns the time it was generated.
     */
    public val timestamp: Long
        get() {
            check(version == 1) { fatal("Uuid is not a time-based Uuid. See Uuid::version.") }
            return mostSignificantBits and 0x0FFFL shl 48 or (mostSignificantBits shr 16 and 0x0FFFFL shl 32) or (mostSignificantBits ushr 32)
        }

    public override fun toString(): String {
        return digits(mostSignificantBits shr 32, 8) + "-" +
                digits(mostSignificantBits shr 16, 4) + "-" +
                digits(mostSignificantBits, 4) + "-" +
                digits(leastSignificantBits shr 48, 4) + "-" +
                digits(leastSignificantBits, 12)
    }

    /**
     * Gets the first [digits] bytes of [value] as a string padded to length twice [digits]
     */
    private fun digits(value: Long, digits: Int): String {
        val mask = (1L shl (digits * 4)) - 1
        return (value and mask).toString(16).padStart(digits, '0')
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Uuid::class)
    public object PrimitiveSerializer : KSerializer<Uuid> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Uuid) {
            val string = value.toString()
            encoder.encodeString(string)
        }

        override fun deserialize(decoder: Decoder): Uuid {
            val string = decoder.decodeString()
            return Uuid(string)
        }
    }

}