/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.bukkit

import org.karrat.World
import org.karrat.entity.Entity
import org.karrat.entity.EntityLiving
import org.karrat.entity.Player

public fun Player.delegate(): PlayerDelegate = PlayerDelegate(this)

public fun Entity.delegate(): EntityDelegate = EntityDelegate(this)

public fun EntityLiving.delegate(): EntityLivingDelegate = EntityLivingDelegate(this)

public fun World.delegate(): WorldDelegate = WorldDelegate(this)