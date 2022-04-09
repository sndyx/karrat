/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.Config
import org.karrat.Server

public fun Server.tps(): Float =
    1000.0f / maxOf((1000 / Config.tps).toLong(), tickTimeMillis)

public fun Server.broadcast(message: String): Unit =
    players.forEach { it.sendMessage(message) }