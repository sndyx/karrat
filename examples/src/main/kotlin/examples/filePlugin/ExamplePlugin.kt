/*
 * Copyright © Karrat - 2022.
 */
@file:Plugin("example-plugin", "Example Plugin File", "1.18.1")
@file:DependsOn("Essentials", "WorldEdit")

package examples.filePlugin

import org.karrat.Server
import org.karrat.play.ticks
import org.karrat.plugin.Init
import org.karrat.plugin.DependsOn
import org.karrat.plugin.Plugin
import org.karrat.server.mtps
import org.karrat.server.scheduleEvery

@Init
fun init() {
    Server.scheduleEvery(20.ticks) {
        println(Server.mtps())
    }
}