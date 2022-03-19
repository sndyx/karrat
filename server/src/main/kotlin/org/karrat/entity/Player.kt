/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.entity

import org.karrat.Server
import org.karrat.World
import org.karrat.network.Session
import org.karrat.network.SocketChannel
import org.karrat.play.Location
import org.karrat.struct.Uuid
import org.karrat.struct.id

public open class Player(
    public val session: Session,
    public val uuid: Uuid,
    public open var name: String,
    public var skin: String = "",
    location: Location
) : EntityLiving(location) {

    override var maxHealth: Double = 20.0

    public fun sendMessage(message: String) {
        session.send(TODO())
    }

    public fun disconnect(message: String): Unit =
        session.disconnect(message)

}

/**
 * A dummy player with a [session] that ignores all read/write
 * operations, by means of [SocketChannel.Dummy].
 */
public class DummyPlayer(
    uuid: Uuid,
    name: String,
    location: Location = Location(
        World(id("TestNameSpace", "Main_World")),
        0.0,
        0.0,
        0.0
    )
) : Player(
    session = Session(SocketChannel.Dummy),
    uuid = uuid,
    name = name,
    location = location
)

public fun Player(uuid: Uuid): Player {
    return Server.players.first { it.uuid == uuid }
}

public fun Player(name: String): Player {
    return Server.players.first { it.name == name }
}