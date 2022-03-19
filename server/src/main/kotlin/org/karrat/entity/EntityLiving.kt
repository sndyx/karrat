/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.entity

import org.karrat.play.Location

public abstract class EntityLiving(location: Location) : Entity(location) {

    public abstract var maxHealth: Double
    public var health: Double = maxHealth

    public fun damage(amount: Double) {
        check(amount >= 0) { "Damage must not be negative." }
        health -= amount
    }

}