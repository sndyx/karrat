/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import org.karrat.struct.getValue

public var ItemStack.name: String
    get() = nbt.getValue("name")
    set(value) {
        nbt["name"] = value
    }

public var ItemStack.lore: List<String>
    get() = nbt.getValue("list") ?: listOf()
    set(value) {
        nbt["list"] = value
    }