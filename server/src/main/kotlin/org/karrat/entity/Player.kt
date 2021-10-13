/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.play.Location
import org.karrat.server.players
import org.karrat.struct.Uuid

public open class Player(
    public val uuid: Uuid
) : EntityLiving() {

    //TODO steve texture
    public lateinit var texture: String

    override var maxHealth: Double = 20.0
    public open val name : String = TODO()
    
}

//Temporary for now ig
public class FakePlayer(uuid: Uuid, override val name: String) : Player(uuid,)

public fun findPlayer(uuid: Uuid): Player? {
    return Server.players().firstOrNull { it.uuid == uuid }
}