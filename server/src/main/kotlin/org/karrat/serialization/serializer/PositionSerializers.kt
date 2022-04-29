/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.serialization.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.karrat.play.*

public object Vec2iSerializer : KSerializer<Vec2i> {
    
    override val descriptor: SerialDescriptor = TODO()
    
    override fun serialize(encoder: Encoder, value: Vec2i) {
        TODO()
    }
    
    override fun deserialize(decoder: Decoder): Vec2i {
        TODO()
    }
    
}

public object Vec2dSerializer : KSerializer<Vec2d> {
    
    override val descriptor: SerialDescriptor = TODO()
    
    override fun serialize(encoder: Encoder, value: Vec2d) {
        TODO()
    }
    
    override fun deserialize(decoder: Decoder): Vec2d {
        TODO()
    }
    
}

public object Vec3iSerializer : KSerializer<Vec3i> {
    
    override val descriptor: SerialDescriptor = TODO()
    
    override fun serialize(encoder: Encoder, value: Vec3i) {
        TODO()
    }
    
    override fun deserialize(decoder: Decoder): Vec3i {
        TODO()
    }
    
}

public object Vec3dSerializer : KSerializer<Vec3d> {
    
    override val descriptor: SerialDescriptor = TODO()
    
    override fun serialize(encoder: Encoder, value: Vec3d) {
        TODO()
    }
    
    override fun deserialize(decoder: Decoder): Vec3d {
        TODO()
    }
    
}

public object LocationSerializer : KSerializer<Location> {
    
    override val descriptor: SerialDescriptor = TODO()
    
    override fun serialize(encoder: Encoder, value: Location) {
        TODO()
    }
    
    override fun deserialize(decoder: Decoder): Location {
        TODO()
    }
    
}