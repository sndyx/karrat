/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.play.Location
import org.karrat.struct.Identifier
import org.karrat.struct.Uuid
import org.karrat.struct.id

public object Config {
    public var preventProxyConnections: Boolean = false
    public var compressionThreshold: Int = 1000
    public var spawnLocation: Location = Location(World(id("minecraft:Main_World")), 0.0, 0.0, 0.0)

    /*
        I didn't test the banning feature before adding it to the code and I'm not testing it now so I
        have forcifully delegated it :)

        Remove this comment after results are reached, and probably the uuid too
     */
    public var bannedPlayers: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"))
    public var tps: Int = 20

    public var motd: String = "Hello Kevster109"
    public var legacymotd: String = "Legacy Clients suck!!! Switch to a newer version y ou loser"
}