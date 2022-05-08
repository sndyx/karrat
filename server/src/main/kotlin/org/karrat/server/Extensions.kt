/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Config
import org.karrat.Server

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