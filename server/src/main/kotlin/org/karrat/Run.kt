/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.server.warning

internal fun main(args: Array<String>) {
    var port = 25565
    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "-port" -> {
                val parsed = (args[i + 1].toShortOrNull())?.toInt()
                port = if (parsed == null) {
                    warning("usage: Karrat [-port <Short>]")
                    port
                } else {
                    parsed
                }
                i++
            }
            else -> warning("usage: Karrat [-port <Short>]")
        }
    }
    Server.start(port)
}