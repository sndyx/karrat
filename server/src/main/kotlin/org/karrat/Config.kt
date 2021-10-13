/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.play.Location

public object Config {
    
    public const val preventProxyConnections: Boolean = false
    public const val compressionThreshold: Int = 1000
    public val spawnLocation: Location = Location(World("Test"), 0.0, 0.0, 0.0)
    
}