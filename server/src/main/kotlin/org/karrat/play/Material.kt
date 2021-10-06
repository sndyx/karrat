/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.NbtCompound
import org.karrat.struct.nbtOf

@Serializable
sealed class Material(val identifier: String) {
    
    internal open val nbt: NbtCompound get() = nbtOf()
    
    object AcaciaButton    : Material("minecraft:acacia_button")
    object AcaciaDoor      : Material("minecraft:acacia_door")
    object AcaciaFence     : Material("minecraft:acacia_fence")
    object AcaciaFenceGate : Material("minecraft:acacia_fence_gate")
    object AcaciaLeaves    : Material("minecraft:acacia_leaves")
    
}

fun Material.defaultNbt(): NbtCompound = nbt