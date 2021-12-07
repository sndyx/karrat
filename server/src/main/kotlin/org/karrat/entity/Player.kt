/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.World
import org.karrat.play.Location
import org.karrat.struct.id
import org.karrat.struct.Uuid

public open class Player(
    public val uuid: Uuid,
    public open var name : String,
    public var skin: String = "",
    location: Location
    ) : EntityLiving(location) {

    // TODO steve texture
    override var maxHealth: Double = 20.0

    public fun sendMessage(message: String) {
    }
    
}

// Temporary for now ig - might actually be used for having server controlled players but idk
public class FakePlayer(uuid: Uuid, name: String) : Player(uuid, name, location = Location(World(id("TestNameSpace", "Main_World")), 0.0, 0.0, 0.0))

public fun Player(uuid: Uuid): Player {
    return Server.players.first { it.uuid == uuid }
}

public fun Player(name: String): Player {
    return Server.players.first { it.name == name }
}