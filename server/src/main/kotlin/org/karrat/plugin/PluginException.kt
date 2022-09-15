/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

public open class PluginException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause)