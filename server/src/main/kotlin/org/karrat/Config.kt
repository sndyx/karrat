/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import org.karrat.play.Location
import org.karrat.play.colored
import org.karrat.struct.Uuid
import org.karrat.struct.id

public object Config {

    public fun plugins(block: () -> Unit) {
        //temp
    }
    
    public var mainThreadId: Long = Thread.currentThread().id
    
    public var sessionServer: String = "https://sessionserver.mojang.com"
    public var preventProxyConnections: Boolean = false
    public var compressionThreshold: Int = 1000
    public var networkBufferSize: Int = 32767
    public val spawnLocation: Location by lazy { Location(World(id("minecraft:Main_World")), 0.0, 0.0, 0.0) }
    public var viewDistance: Int = 8
    public var simulationDistance: Int = 8
    
    public var isWhitelistOnly: Boolean = false
    public var whitelist: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"))
    public var bannedPlayers: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"))
    public var tps: Int = 20

    public var maxPlayers: Int = 100
    public var versionName: String = "1.18.1"
    public var motd: String = "Hello Kevster109"
    public var legacyMotd: String = "Legacy Clients suck!!! Switch to a newer version y ou loser"

    public var colorOutput: Boolean = false
    public var port: Int = 25565
    public var basicLogging: Boolean = false

    public var ignoreCommandCapitalization: Boolean = true
    public var commandNotFoundMessage: String = "&cCommand not found.".colored()
    public var invalidSyntaxMessage: String = "&cInvalid syntax.".colored()

}
