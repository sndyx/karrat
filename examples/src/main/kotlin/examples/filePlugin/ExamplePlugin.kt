/*
 * Copyright © Karrat - 2022.
 */
@file:Plugin("Example Plugin File", "1.18.1")
@file:DependsOn("Essentials", "WorldEdit")

package examples.filePlugin

import kotlinx.coroutines.delay
import org.karrat.Server
import org.karrat.play.ticks
import org.karrat.plugin.Init
import org.karrat.plugin.DependsOn
import org.karrat.plugin.Plugin
import org.karrat.server.mtps
import org.karrat.server.parallelize
import org.karrat.server.scheduleEvery
import kotlin.random.Random

@Init
fun init() {
    Server.parallelize(dataEntries()) {
        readEntry(it) // Expensive computation
    }
    Server.scheduleEvery(20.ticks) {
        println(Server.mtps())
    }
}

fun dataEntries(): List<String> {
    return listOf("")
}

suspend fun readEntry(fileName: String) {
    delay(Random(0).nextLong(1, 1000))
}