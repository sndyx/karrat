/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.entity

import org.karrat.struct.Location
import org.karrat.struct.Vec3d

public abstract class Entity(loc: Location) {

    public val id: Int = loc.world.entities
        .map { it.id }
        .foldRightIndexed<Int, Int?>(null) { index, id, acc -> if (acc == null && index != id) index else null } ?: 0

    internal var velocityChanged: Boolean = false
    internal var locationChanged: Boolean = false

    public var location: Location = loc
    set(value) {
        field = value
        locationChanged = true
    }

    public var velocity: Vec3d = Vec3d(0.0, 0.0, 0.0)
    set(value) {
        field = value
        velocityChanged = true
    }

    public var gravity: Boolean = true
    public var friction: Boolean = true
    public var invulnerable: Boolean = false

    public open fun remove() {
        location.world.entities.remove(this)
    }

}