/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.play.Location
import org.karrat.server.fatal

abstract class EntityLiving(location: Location) : Entity(location) {
    
    abstract var maxHealth: Double
    var health = maxHealth
    
    fun damage(amount: Double) {
        check(amount >= 0) { fatal("Damage must not be negative.") }
        health -= amount
    }

}