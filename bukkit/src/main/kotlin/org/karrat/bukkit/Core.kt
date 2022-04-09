/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.bukkit

import org.bukkit.Bukkit
import org.karrat.plugin.Plugin

@Plugin("Karrat/Bukkit", "1.0")
public object Core {

    init {
        Bukkit.setServer(ServerDelegate)
    }

}