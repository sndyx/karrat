/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.world

import org.karrat.World
import org.karrat.entity.Player

fun World.players(): List<Player> = entities.filterIsInstance<Player>()