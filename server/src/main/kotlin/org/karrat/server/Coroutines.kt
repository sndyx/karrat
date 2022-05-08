/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.karrat.Server
import kotlin.time.Duration

public fun Server.schedule(wait: Duration, action: suspend () -> Unit) {
    launch {
        delay(wait.inWholeMilliseconds)
        action()
    }
}

public fun Server.scheduleEvery(wait: Duration, action: suspend () -> Unit): Job {
    return launch {
        while (isActive) {
            delay(wait.inWholeMilliseconds)
            action()
        }
    }
}