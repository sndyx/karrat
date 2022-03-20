/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.entity

import org.karrat.play.Location
import org.karrat.play.Vec3

public abstract class Entity(loc: Location) {

    internal var velocityChanged: Boolean = false
    internal var locationChanged: Boolean = false

    public var location: Location = loc
    set(value) {
        field = value
        locationChanged = true
    }

    public var velocity: Vec3 = Vec3(0.0, 0.0, 0.0)
    set(value) {
        field = value
        velocityChanged = true
    }

    public var gravity: Boolean = true
    public var friction: Boolean = true
    public var invulnerable: Boolean = false

    public open fun remove() {

    }

}