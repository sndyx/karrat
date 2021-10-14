/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.play.Location
import org.karrat.struct.Identifier

public object Config {
    
    public const val preventProxyConnections: Boolean = false
    public const val compressionThreshold: Int = 1000
    public val spawnLocation: Location = Location(World(Identifier("TestNamespace","Test")), 0.0, 0.0, 0.0)
    
}