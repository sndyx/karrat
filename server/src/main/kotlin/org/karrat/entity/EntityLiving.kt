/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.play.Location

abstract class EntityLiving(location: Location) : Entity(location) {
    
    abstract var maxHealth: Double
    var health = maxHealth
    
    fun damage(amount: Double) {
        check(amount >= 0) { "Damage must not be negative." }
        health -= amount
    }

}