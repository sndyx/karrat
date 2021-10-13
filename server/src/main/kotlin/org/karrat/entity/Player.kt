/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.play.Location
import org.karrat.server.players
import org.karrat.struct.Uuid

public open class Player(
    public val uuid: Uuid,
    location: Location
) : EntityLiving(location) {
    
    override var maxHealth: Double = 20.0
    public open val name : String = TODO()
    
}

//Temporary for now ig
public class FakePlayer(uuid: Uuid, override val name: String, location: Location) : Player(uuid, location)

public fun Player(uuid: Uuid): Player {
    return Server.players().firstOrNull { it.uuid == uuid } ?: error { "Player does not exist." }
}