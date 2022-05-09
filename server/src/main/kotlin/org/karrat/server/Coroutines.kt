/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import org.karrat.Server
import kotlin.time.Duration

public fun Server.schedule(wait: Duration, action: suspend () -> Unit) {
    launch {
        delay(wait)
        action()
    }
}

public fun Server.scheduleEvery(wait: Duration, action: suspend () -> Unit): Job {
    return launch {
        while (isActive) {
            delay(wait)
            action()
        }
    }
}

public suspend inline fun <T, R> Server.parallelize(
    items: Collection<T>,
    crossinline action: suspend (T) -> R
): Unit = with(Server) { // This is stupid
    items.map {
        async { action(it) }
    }.awaitAll()
}