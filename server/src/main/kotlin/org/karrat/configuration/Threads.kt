/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.configuration

import kotlinx.coroutines.*
import org.karrat.Config
import org.karrat.Server

@OptIn(DelicateCoroutinesApi::class)
public val Server.threadPool: ExecutorCoroutineDispatcher? by lazy {
    println("Created fixed thread pool with ${Config.threadCount} threads.")
    if (Config.threadCount > 0) newFixedThreadPoolContext(Config.threadCount, "worker-thread")
    else null
}

public fun CoroutineScope.launchInThreadPool(block: suspend CoroutineScope.() -> Unit): Job =
    if (Server.threadPool != null) launch(Server.threadPool!!) { block() }
    else launch { block() }