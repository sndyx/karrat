/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.examples

import org.karrat.Server
import org.karrat.event.PacketEvent
import org.karrat.event.StatusResponseEvent
import org.karrat.event.on
import org.karrat.packet.serverbound.ServerboundPacket
import org.karrat.plugin.Enable
import org.karrat.plugin.InitializeAfter
import org.karrat.plugin.Plugin
import org.karrat.server.info
import kotlin.io.path.Path
import kotlin.io.path.readBytes

@Plugin(name="Example Plugin", version="1.17.1")
@InitializeAfter("Essentials", "WorldEdit")
object ExamplePlugin {
    
    @Enable
    fun enable() {
        Server.on<PacketEvent<ServerboundPacket>> { info(it) }

        //Lie to client pog. at least when this is implemented
        Server.on<StatusResponseEvent> {
            it.response.maxPlayers = 30
            it.response.image = Path("/image.png").readBytes()
        }
    }
    
}