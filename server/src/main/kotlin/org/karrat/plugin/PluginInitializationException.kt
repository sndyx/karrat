/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

public class PluginInitializationException(
    message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException(message)