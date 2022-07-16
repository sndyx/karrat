/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.entity

import org.karrat.internal.lazyMutable
import org.karrat.struct.Location

public abstract class EntityLiving(location: Location) : Entity(location) {

    public abstract var maxHealth: Double
    public var health: Double by lazyMutable { maxHealth }

    public fun damage(amount: Double) {
        require(amount >= 0) { "Damage must not be negative." }
        health -= amount
    }

}