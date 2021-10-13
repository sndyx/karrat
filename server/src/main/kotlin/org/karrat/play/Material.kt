/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.NbtCompound
import org.karrat.struct.nbtOf

@Serializable
public sealed class Material(public val identifier: String) {
    
    internal open val nbt: NbtCompound get() = nbtOf()
    
    public object AcaciaButton    : Material("minecraft:acacia_button")
    public object AcaciaDoor      : Material("minecraft:acacia_door")
    public object AcaciaFence     : Material("minecraft:acacia_fence")
    public object AcaciaFenceGate : Material("minecraft:acacia_fence_gate")
    public object AcaciaLeaves    : Material("minecraft:acacia_leaves")
    
}

public fun Material.defaultNbt(): NbtCompound = nbt