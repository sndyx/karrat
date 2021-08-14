/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

import org.karrat.network.INetHandler

interface ServerboundPacket {
    
    fun process(handler: INetHandler)
    
}