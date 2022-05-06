/*
 * Copyright © Karrat - 2022.
 */

package examples.basic

import org.karrat.Server
import org.karrat.event.StatusResponseEvent
import org.karrat.event.on
import org.karrat.plugin.Init
import org.karrat.plugin.DependsOn
import org.karrat.plugin.Plugin

@Plugin(name="Example Plugin", version="1.18.1")
@DependsOn("Essentials", "WorldEdit")
object ExamplePlugin {
    
    @Init
    fun enable() {
        Server.on<StatusResponseEvent> {
            it.response.maxPlayers = 30
        }
    }
    
}