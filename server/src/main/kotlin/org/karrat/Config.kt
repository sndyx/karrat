/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.play.Location
import org.karrat.struct.Uuid
import org.karrat.struct.id

public object Config {

    public var sessionServer: String = "https://sessionserver.mojang.com"
    public var preventProxyConnections: Boolean = false
    public var compressionThreshold: Int = 1000
    public var spawnLocation: Location = Location(World(id("minecraft:Main_World")), 0.0, 0.0, 0.0)
    public var bannedPlayers: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"))
    public var tps: Int = 20
    
    public var maxPlayers: Int = 100
    public var versionName: String = "1.18.1"
    public var motd: String = "Hello Kevster109"
    public var legacyMotd: String = "Legacy Clients suck!!! Switch to a newer version y ou loser"

    public var colorOutput: Boolean = false
    public var port: Int = 25565
    
}