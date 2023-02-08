/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.sided

import org.karrat.Server
import org.karrat.entity.Player

public fun runExclusively(player: Player, action: ExclusiveScope.() -> Unit) {
    val scope = ExclusiveScope(player)
    scope.action()
}

public fun exclusiveScope(player: Player): ExclusiveScope {
    val scope = ExclusiveScope(player)
    return scope
}

public fun test() {
    // val player = Server.players.first()
    // runExclusively(player) {

    // }
    // val scope = exclusiveScope(player)
}