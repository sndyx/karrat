/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.NbtCompound

@Serializable
public class ItemStack(
    public val material: Material,
    public val amount: Int = 1
) {


    public var nbt: NbtCompound = NbtCompound()

}