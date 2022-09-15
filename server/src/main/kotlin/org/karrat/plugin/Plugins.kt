/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import org.karrat.Config
import org.karrat.Server

internal val loadedPlugins: MutableList<PluginClass> = mutableListOf()
public val Server.plugins: List<Plugin> get() = loadedPlugins.map { it.plugin }

public val Plugin.Companion.minecraft: Plugin get() =
    Plugin("minecraft", "Minecraft", Config.versionName)

public val Plugin.Companion.karrat: Plugin get() =
    Plugin("karrat", "Karrat", Config.versionName)

public fun Plugin(id: String): Plugin =
    Server.plugins.first { it.id == id }

public val Plugin.surrogate: PluginClass get() = loadedPlugins.first { it.plugin.id == id }
public val Plugin.dependants: List<String> get() = surrogate.dependsOn?.plugins?.toList() ?: emptyList()

public fun Plugin.init() {
    surrogate.init()
}

public fun Plugin.exit() {
    surrogate.exit()
}