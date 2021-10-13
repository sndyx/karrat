/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.play.Location
import org.karrat.server.fatal

public abstract class EntityLiving : Entity() {
    public abstract var maxHealth: Double
    public var health: Double = maxHealth
    
    public fun damage(amount: Double) {
        check(amount >= 0) { fatal("Damage must not be negative.") }
        health -= amount
    }

}