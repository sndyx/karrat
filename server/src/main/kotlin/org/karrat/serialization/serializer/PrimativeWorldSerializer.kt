/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.serialization.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.karrat.Server
import org.karrat.World
import org.karrat.struct.id

public object PrimitiveWorldSerializer : KSerializer<World> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("World", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: World) {
        val string = value.identifier.toString()
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): World {
        val identifier = id(decoder.decodeString())
        return Server.worlds.firstOrNull { it.identifier == identifier }
            ?: error("Unable to find world.")
    }

}