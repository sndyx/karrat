/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.play.Location
import org.karrat.server.players
import org.karrat.struct.Uuid

class Player(val uuid: Uuid, location: Location) : EntityLiving(location) {
    
    override var maxHealth = 20.0
    val name: String = TODO()
    
}

fun Player(uuid: Uuid): Player {
    return Server.players().firstOrNull { it.uuid == uuid } ?: error { "Player does not exist." }
}