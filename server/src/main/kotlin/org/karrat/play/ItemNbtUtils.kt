/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import org.karrat.play.ItemStack
import org.karrat.struct.getAs

var ItemStack.name: String
    get() = nbt.getAs("name")
    set(value) {
        nbt["name"] = value
    }

var ItemStack.lore: List<String>
    get() = nbt.getAs("list") ?: listOf()
    set(value) {
        nbt["list"] = value
    }