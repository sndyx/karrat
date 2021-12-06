/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player

public fun Server.players(): Set<Player> {
    TODO()
}

public fun Server.tps(): Float =
    1000.0f / maxOf((1000 / Config.tps).toLong(), tickTimeMillis)