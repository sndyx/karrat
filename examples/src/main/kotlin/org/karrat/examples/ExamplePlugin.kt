/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.examples

import org.karrat.Server
import org.karrat.event.PacketEvent
import org.karrat.event.on
import org.karrat.plugin.Enable
import org.karrat.plugin.InitializeAfter
import org.karrat.plugin.Plugin
import org.karrat.server.info

@Plugin(name="Example Plugin", version="1.17.1")
@InitializeAfter("Essentials", "WorldEdit")
object ExamplePlugin {
    
    @Enable
    fun enable() {
        Server.on<PacketEvent<*>> { info(it) }
    }
    
}