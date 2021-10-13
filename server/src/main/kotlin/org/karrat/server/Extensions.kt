/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import org.karrat.Server
import org.karrat.entity.Player

public fun Server.players(): List<Player> {
    TODO()
}

public fun Server.tps(): Float =
    1000.0f / maxOf(50L, tickTimeMillis)