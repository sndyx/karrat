/*
 * Copyright © Karrat - 2023.
 */
@file:Plugin("example-plugin", "ExamplePlugin", "1.18.1")
@file:DependsOn("essentials", "worldedit")

package examples.filePlugin

import org.karrat.Server
import org.karrat.server.ticks
import org.karrat.plugin.Init
import org.karrat.plugin.DependsOn
import org.karrat.plugin.Plugin
import org.karrat.server.mtps
import org.karrat.server.scheduleEvery

@Init
fun init() {
    Server.scheduleEvery(200.ticks) {
        println(Server.mtps())
        Server.worlds.single().chunks.values.forEach {
            it.sections.forEach {
                println(it.data.data.toList())
            }
        }
    }
}