/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.plugin

public class PluginInitializationException(
    message: String? = null,
    cause: Throwable? = null
) : PluginException(message, cause)