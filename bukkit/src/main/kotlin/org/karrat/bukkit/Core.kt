/*
 * Copyright © Karrat - 2022.
 */
@file:Plugin("Karrat/Bukkit", "1.0")

package org.karrat.bukkit

import org.bukkit.Bukkit
import org.karrat.plugin.Init
import org.karrat.plugin.Plugin

@Init
public fun init() {
    Bukkit.setServer(ServerDelegate)
}