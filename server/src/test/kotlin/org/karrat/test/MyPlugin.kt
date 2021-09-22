/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.test

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.karrat.Server
import org.karrat.World
import org.karrat.entity.Entity
import org.karrat.entity.Player
import org.karrat.event.*
import org.karrat.network.ServerSocket
import org.karrat.plugin.InitializeAfter
import org.karrat.plugin.Plugin
import org.karrat.play.BlockPos
import org.karrat.play.Location
import org.karrat.server.log
import org.karrat.server.players

@InitializeAfter("essentials")
@Plugin("Example", "1.4.3")
object MyPlugin {
    
    init {
        
        println("Hello from ExamplePlugin!")
        
        val world = World("MainWorld")
        
        val pos = BlockPos(1, 2, 3) // Implements Vec3
        val loc = Location(world, 1.0, 2.0, 3.0) // Implements Vec3
        
        Server.players()
            .filter { it.name.startsWith('s') }
            .map<Player, Entity> { it }
            .forEach {
                println(it.location.x)
            }
        
        Server.events
            .filterIsInstance<PacketEvent<*>>()
            .onEach { log(it) }
            .launchIn(ServerSocket)
        
        Server.on<PacketEvent<*>> {
            log(it)
        }
        
        Server.on<Event> {
            dispatchEvent(it) // Infinite recursive loop :)
        }
        
    }
    
}