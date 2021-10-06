/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.internal.forEach
import org.karrat.server.warning

fun main(args: Array<String>) {
    var port = 25565
    args.forEach {
        when (it) {
            "-port" -> {
                val parsed = (args[index + 1].toShortOrNull())?.toInt()
                port = if (parsed == null) {
                    warning("usage: Karrat [-port <Short>]")
                    25565
                } else {
                    parsed
                }
                skip(1)
            }
            else -> warning("usage: Karrat [-port <Short>]")
        }
    }
    Server.start(port)
}