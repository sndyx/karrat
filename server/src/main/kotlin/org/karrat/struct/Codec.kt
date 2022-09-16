/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import kotlinx.serialization.KSerializer
import org.karrat.serialization.nbt.Nbt
import org.karrat.server.Registry

public abstract class Codec<T : Identified> : Registry<T> {

    public abstract val id: Identifier
    public abstract val serializer: KSerializer<T>

    private var cachedCodec: NbtCompound? = null

    public fun codec(): NbtCompound {
        if (cachedCodec != null) return cachedCodec!!
        val codec = NbtCompound()
        codec["type"] = id.toString()
        codec["value"] = list.mapIndexed { index, it ->
            NbtCompound().also { nbt ->
                nbt["id"] = index
                nbt["name"] = id.toString()
                nbt["element"] = Nbt.encodeToNbt(it, serializer)
            }
        }
        cachedCodec = codec
        return codec
    }

}