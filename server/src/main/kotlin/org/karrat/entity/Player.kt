/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.play.Location
import org.karrat.util.Uuid

class Player(val uuid: Uuid, location: Location) : EntityLiving(location) {
    
    override var maxHealth = 20.0
    val name: String = TODO()
    
}