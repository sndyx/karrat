/*
 * Copyright © Karrat - 2022.
 */

package examples.basic

import org.karrat.Server
import org.karrat.event.PacketEvent
import org.karrat.event.StatusResponseEvent
import org.karrat.event.on
import org.karrat.packet.ServerboundPacket
import org.karrat.plugin.Init
import org.karrat.plugin.InitializeAfter
import org.karrat.plugin.Plugin

@Plugin(name="Example Plugin", version="1.18.1")
@InitializeAfter("Essentials", "WorldEdit")
object ExamplePlugin {
    
    @Init
    fun enable() {
        Server.on<StatusResponseEvent> {
            it.response.maxPlayers = 30
        }
    }
    
}