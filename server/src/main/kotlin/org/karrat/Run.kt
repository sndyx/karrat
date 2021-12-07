/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.server.warning
import org.karrat.struct.Uuid
import kotlin.math.atan
import kotlin.math.sin

internal fun main(args: Array<String>) {
    var port = 25565
    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "-port" -> {
                val parsed = (args[i + 1].toShortOrNull())?.toInt()
                port = if (parsed == null) {
                    warning("Usage: Karrat [-port <Short>]")
                    port
                } else {
                    parsed
                }
                i++
            }
            else -> warning("Usage: Karrat [-port <Short>]")
        }
    }
    Server.start(port)
}