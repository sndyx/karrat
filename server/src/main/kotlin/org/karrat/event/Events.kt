/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.karrat.Server
import org.karrat.network.ServerSocket

internal val eventFlow =
    MutableSharedFlow<Event>(replay = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST).apply {
        distinctUntilChanged()
    }

fun dispatchEvent(event: Event): Boolean {
    eventFlow.tryEmit(event)
    return if (event is CancellableEvent) event.isCancelled
    else false
}

val Server.events: SharedFlow<Event>
    get() = eventFlow.asSharedFlow()

inline fun <reified T : Event> Server.on(crossinline block: (T) -> Unit) {
    events
        .filterIsInstance<T>()
        .onEach { block.invoke(it) }
        .launchIn(ServerSocket)
}