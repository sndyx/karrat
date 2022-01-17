/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.struct.NbtCompound
import org.karrat.struct.getCompound
import org.karrat.struct.getValue
import org.karrat.struct.getValueOrDefault

@Serializable
public class ItemStack private constructor() {
    
    public constructor(
        material: Material,
        amount: Int = 1
    ) : this() {
        this.material = material
        count = amount
    }
    
    public var nbt: NbtCompound = NbtCompound()
    
    // Nbt property accessors:
    
    public var material: Material
        get() = nbt.getValue("id")
        set(value) {
            nbt["id"] = value.identifier.toString()
        }
    
    public var count: Int
        get() = nbt.getValue<Byte>("Count").toInt()
        set(value) {
            nbt["Count"] = value.toByte()
        }
    
    public var name: String
        get() = nbt.getValueOrDefault("name", "") // Return default name if absent?
        set(value) {
            nbt["name"] = value
        }
    
    public var description: List<String>
        get() = nbt.getValueOrDefault("list", emptyList())
        set(value) {
            nbt["list"] = value
        }
    
    public var damage: Int
        get() = nbt.getCompound("tag").getValue("Damage")
        set(value) {
            nbt.getCompound("tag")["Damage"] = value
        }
    
    public var unbreakable: Boolean
        get() = nbt.getValue("unbreakable")
        set(value) {
            nbt["unbreakable"] = value
        }
    
}