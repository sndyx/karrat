/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlinx.coroutines.*
import org.karrat.Config
import org.karrat.Server

@OptIn(DelicateCoroutinesApi::class)
public val Server.threadPool: ExecutorCoroutineDispatcher? by lazy {
    if (Config.threadCount > 1) newFixedThreadPoolContext(Config.threadCount, "worker-thread")
    else null
}

public fun CoroutineScope.launchInThreadPool(block: suspend CoroutineScope.() -> Unit): Job =
    if (Server.threadPool != null) launch(Server.threadPool!!) { block() }
    else launch { block() }