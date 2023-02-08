/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.plugin

import org.karrat.Config
import org.karrat.Server

public val Plugin.PluginRegistry.minecraft: Plugin get() =
    Plugin("minecraft", "Minecraft", Config.versionName)

public val Plugin.PluginRegistry.karrat: Plugin get() =
    Plugin("karrat", "Karrat", Config.versionName)

public fun Plugin(id: String): Plugin =
    Server.plugins.first { it.id == id }

public val Plugin.surrogate: PluginClass get() = Plugin.classLoaders.first { it.pluginClass.plugin.id == id }.pluginClass
public val Plugin.dependants: List<String> get() = surrogate.dependsOn?.plugins?.toList() ?: emptyList()

public fun Plugin.init() {
    surrogate.init()
}

public fun Plugin.exit() {
    surrogate.exit()
}