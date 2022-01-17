/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.world.dimensions

public enum class GameMode {

    SURVIVAL,
    ADVENTURE,
    SPECTATOR,
    CREATIVE;

    public fun toId(): Byte = when (this) {
        SURVIVAL -> 0
        ADVENTURE -> 1
        SPECTATOR -> 2
        CREATIVE -> 3
    }

}