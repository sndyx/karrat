/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.configuration

import kotlinx.coroutines.*
import org.karrat.Config

@OptIn(DelicateCoroutinesApi::class)
public val threadPool: ExecutorCoroutineDispatcher? by lazy {
    if (Config.threadCount > 1) {
        newFixedThreadPoolContext(Config.threadCount - 1, "worker-thread")
    } else null
}

public fun launchWithThreadCount(block: suspend CoroutineScope.() -> Unit): Job? = runBlocking {
    threadPool?.run {
        launch(threadPool!!) {
            block()
        }
    } ?: run {
        block()
        null
    }
}
