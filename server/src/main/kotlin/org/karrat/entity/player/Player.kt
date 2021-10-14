/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.World
import org.karrat.play.Location
import org.karrat.server.players
import org.karrat.struct.Identifier
import org.karrat.struct.Uuid

public open class Player(
    public val uuid: Uuid,
    public open var name : String,
    location: Location,
    public var skin: String = ""
) : EntityLiving(location) {

    //TODO steve texture
    override var maxHealth: Double = 20.0

}

//Temporary for now ig
public class FakePlayer(uuid: Uuid, name: String) : Player(uuid, name, Location(World(Identifier("TestNameSpace", "Main_World")), 0.0, 0.0, 0.0))

public fun Player(uuid: Uuid): Player {
    return Server.players().first { it.uuid == uuid }
}

public fun Player(name: String): Player {
    return Server.players().first { it.name == name }
}