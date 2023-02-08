/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.event

import org.karrat.Server
import kotlin.reflect.KClass

/**
 * Dispatches an [Event] to all consumers. if event is [CancellableEvent] and
 * [CancellableEvent.isCancelled] is true, caller should handle accordingly.
 * Otherwise, will always return false.
 */
@Suppress("Unused")
public fun Server.dispatchEvent(event: Event): Boolean {
    consumers
        .filter { it.first.isInstance(event) }
        .map { it.second }
        .forEach { it.invoke(event) }
    return if (event is CancellableEvent) event.isCancelled
    else false
}

@PublishedApi
internal val consumers: MutableList<Pair<KClass<*>, (Event) -> Unit>> = mutableListOf()

@Suppress("Unchecked_Cast")
public inline fun <reified T : Event> Server.on(noinline consumer: (T) -> Unit) {
    consumers.add(Pair(T::class, consumer as (Event) -> Unit))
}

public fun CancellableEvent.cancel() {
    isCancelled = true
}