/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.World
import org.karrat.play.Location
import org.karrat.server.players
import org.karrat.struct.Uuid

public open class Player(
    public val uuid: Uuid,
    location: Location,
    public var skin: String = ""
) : EntityLiving(location) {

    //TODO steve texture
    override var maxHealth: Double = 20.0
    public open val name : String = TODO()
    
}

//Temporary for now ig
public class FakePlayer(uuid: Uuid, override val name: String) : Player(uuid, Location(World("Main_World"), 0.0, 0.0, 0.0))

public fun Player(uuid: Uuid): Player {
    return Server.players().first { it.uuid == uuid }
}

public fun Player(name: String): Player {
    return Server.players().first { it.name == name }
}