/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import org.karrat.plugin.Plugin

public interface Registry<T> {

    public val list: MutableList<T>

    context(Plugin)
    public fun register(value: T)

    context(Plugin)
    public fun unregister(value: T)

    public fun load()

}