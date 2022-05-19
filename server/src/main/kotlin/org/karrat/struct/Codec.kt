/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import org.karrat.server.Registry

public abstract class Codec<T>(public val id: Identifier) : Registry<T> {

    public var codec: NbtCompound? = null

    public fun codec(): NbtCompound {
        return if (codec == null) {
            codec = NbtCompound()
            codec!!
        } else codec!!
    }

}