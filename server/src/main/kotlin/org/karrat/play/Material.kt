/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.Identifier
import org.karrat.struct.id

/*
 * File auto-generated by `MaterialGenerator.kt`
 * from `materials.json` source file.
 * DO NOT EDIT!
 */

@JvmInline
@Serializable
public value class Material(public val identifier: Identifier) {

    public companion object {
        public val AcaciaButton: Material = Material(id("minecraft:acacia_button"))
        public val AcaciaDoor: Material = Material(id("minecraft:acacia_door"))
        public val AcaciaFence: Material = Material(id("minecraft:acacia_fence"))
        public val AcaciaFenceGate: Material = Material(id("minecraft:acacia_fence_gate"))
        public val AcaciaLeaves: Material = Material(id("minecraft:acacia_leaves"))
    }
}        
