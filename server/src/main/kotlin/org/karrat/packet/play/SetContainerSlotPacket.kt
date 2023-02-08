/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.packet.play

import org.karrat.packet.ClientboundPacket
import org.karrat.response.SlotData
import org.karrat.struct.DynamicByteBuffer
import org.karrat.struct.NbtCompound
import org.karrat.struct.writeNbt
import org.karrat.struct.writeVarInt

public class SetContainerSlotPacket(
    public val windowId: Byte,
    public val stateId: Int,
    public val slot: Short,
    public val slotData: SlotData?
) : ClientboundPacket {

    override val id: Int = 0x13

    override fun write(data: DynamicByteBuffer) {
        data.write(windowId)
        data.writeVarInt(stateId)
        data.writeShort(slot)
        data.writeBoolean(slotData != null)
        slotData?.let {
            data.writeVarInt(slotData.itemId)
            data.write(slotData.itemCount)
            data.writeNbt(slotData.nbt)
        }
    }

}