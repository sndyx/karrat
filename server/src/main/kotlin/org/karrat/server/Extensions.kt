/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Config
import org.karrat.Server
import org.karrat.command.Command
import org.karrat.play.Material
import org.karrat.world.Biome
import org.karrat.world.Dimension
import kotlin.system.measureTimeMillis

/**
 * Returns the current tps (ticks per second) value.
 */
public fun Server.tps(): Float =
    1000.0f / maxOf((1000 / Config.tps).toLong(), tickTimeMillis)
    
/**
 * Returns the maximum amount of ticks that could take place in a single second,
 * given an uncapped tick-rate.
 */
public fun Server.mtps(): Float =
    1000.0f / tickTimeMillis

/**
 * Sends a given message to every player present in the server.
 */
public fun Server.broadcast(message: String): Unit =
    players.forEach { it.sendMessage(message) }

internal fun Server.loadResources() {
    measureTimeMillis {
        Biome.load()
    }.let { println("Loaded ${Biome.list.size} biomes in ${it}ms.") }
    measureTimeMillis {
        Command.load()
    }.let { println("Loaded ${Command.list.size} commands in ${it}ms.") }
    measureTimeMillis {
        Dimension.load()
    }.let { println("Loaded ${Dimension.list.size} dimensions in ${it}ms.") }
    measureTimeMillis {
        Material.load()
    }.let { println("Loaded ${Material.list.size} materials in ${it}ms.") }
}