/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlinx.coroutines.*
import org.karrat.Server
import kotlin.time.Duration

public fun Server.schedule(after: Duration, action: suspend () -> Unit): Job {
    return launch {
        delay(after)
        action()
    }
}

public fun Server.scheduleEvery(after: Duration, action: suspend () -> Unit): Job {
    return launch {
        while (isActive) {
            delay(after)
            action()
        }
    }
}

public inline fun <T, R> Server.parallelize(
    items: Collection<T>,
    crossinline action: suspend (T) -> R
): List<Deferred<R>> { // T̶h̶i̶s̶ ̶i̶s̶ ̶s̶t̶u̶p̶i̶d̶ This is awesome.
    return items.map {
        async { action(it) }
    }
}