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

    public var bannedPlayers: MutableList<Uuid> = mutableListOf()
    
    public var tps: Int = 20
    
}