/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.NbtCompound

@Serializable
class ItemStack(val material: Material, val amount: Int = 1) {
    
    var nbt = NbtCompound()
    
}