/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.play

import org.karrat.network.Session
import org.karrat.packet.play.SetContainerSlotPacket
import org.karrat.response.SlotData

public abstract class Container : Iterable<ItemStack?> {

    public abstract val id: Byte

    internal abstract val items: Array<ItemStack?>
    public abstract val handles: MutableList<Session>

    public val size: Int get() = items.size

    public operator fun get(index: Int): ItemStack? {
        return items[index]
    }

    public operator fun set(index: Int, value: ItemStack?) {
        val packet = SetContainerSlotPacket(
            id,
            0, // FIXME is this supposed to be a nonce or what
            index.toShort(),
            value?.let { SlotData(0, it.count.toByte(), it.nbt) }
        )
        handles.forEach { it.send(packet) }
        items[index] = value
    }

    override fun iterator(): Iterator<ItemStack?> = items.iterator()

    public fun clear() {
        for (i in items.indices) {
            set(i, null)
        }
    }

}

// @kotlinc stop giving me a warning!!! i want rows to be available
public class Chest(public val rows: Int) : Container() {

    // FIXME something idk
    // Not sure if this is supposed to be an arbitrary value
    // or to describe which type of inventory it is...
    override val id: Byte = 1

    override val handles: MutableList<Session> = mutableListOf()
    override val items: Array<ItemStack?> = arrayOfNulls(9 * rows)

}

public class Inventory : Container() {

    override val id: Byte = 0

    override val handles: MutableList<Session> = mutableListOf()
    override val items: Array<ItemStack?> = arrayOfNulls(46)
    public var itemOnCursor: ItemStack? = null

}