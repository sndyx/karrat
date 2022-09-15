/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

public interface Registry<T> {

    public val list: MutableList<T>

    public fun register(value: T)
    public fun unregister(value: T)
    public fun load()

}