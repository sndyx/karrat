/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.test

import org.karrat.Server
import org.karrat.World
import org.karrat.event.PacketEvent
import org.karrat.event.cancel
import org.karrat.event.on
import org.karrat.util.randomUuid

fun main() {
    Server.start(25565)
    Server.on<PacketEvent<*>> {
        println(it.packet)
        it.cancel()
    }
    World("Main_World").entities
        .forEach { it.remove() }
    randomUuid()
}