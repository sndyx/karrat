/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.response

import org.karrat.struct.NbtCompound

public data class SlotData(
    public val itemId: Int,
    public val itemCount: Byte,
    public val nbt: NbtCompound
)