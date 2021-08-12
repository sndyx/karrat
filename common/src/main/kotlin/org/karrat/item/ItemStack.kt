package org.karrat.item

import kotlinx.serialization.Serializable

@Serializable
class ItemStack(val material: Material, val amount: Int = 1) {
    
    val meta = ItemMeta("")
    
}