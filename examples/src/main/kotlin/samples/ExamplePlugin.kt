/*
 * Copyright © Karrat - 2022.
 */

package samples

import org.karrat.Server
import org.karrat.event.PacketEvent
import org.karrat.event.StatusResponseEvent
import org.karrat.event.on
import org.karrat.packet.ServerboundPacket
import org.karrat.plugin.Enable
import org.karrat.plugin.InitializeAfter
import org.karrat.plugin.Plugin

@Plugin(name="Example Plugin", version="1.17.1")
@InitializeAfter("Essentials", "WorldEdit")
object ExamplePlugin {
    
    @Enable
    fun enable() {
        Server.on<PacketEvent<ServerboundPacket>> { println(it) }

        //Lie to client pog. at least when this is implemented
        Server.on<StatusResponseEvent> {
            it.response.maxPlayers = 30
        }
    }
    
}