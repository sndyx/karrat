/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.Server
import kotlin.reflect.KClass

/**
 * Dispatches an [Event] to all consumers. if event is [CancellableEvent] and
 * [CancellableEvent.isCancelled] is true, caller should handle accordingly.
 * Otherwise, will always return false.
 */
fun dispatchEvent(event: Event): Boolean {
    consumers
        .filter { it.first.isInstance(event) }
        .map { it.second }
        .forEach { it.invoke(event) }
    return if (event is CancellableEvent) event.isCancelled
    else false
}

val consumers = mutableListOf<Pair<KClass<*>, (Event) -> Unit>>()

@Suppress("Unchecked_Cast")
inline fun <reified T : Event> Server.on(noinline consumer: (T) -> Unit) {
    consumers.add(Pair(T::class, consumer as (Event) -> Unit))
}

fun CancellableEvent.cancel() {
    isCancelled = true
}