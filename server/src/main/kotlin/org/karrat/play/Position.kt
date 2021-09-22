/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import org.karrat.World

interface Vec3 {
    
    var x: Double
    var y: Double
    var z: Double
    
}

internal open class Vec3Impl(
    override var x: Double,
    override var y: Double,
    override var z: Double
    ) : Vec3 {
    
    constructor(other: Vec3) : this(other.x, other.y, other.z)
    
}

class BlockPos(x: Int, y: Int, z: Int): Vec3 by Vec3Impl(x.toDouble(), y.toDouble(), z.toDouble()) {
    
    var posX: Int
        get() = x.toInt()
        set(value) {
            x = value.toDouble()
        }
    var posY: Int
        get() = y.toInt()
        set(value) {
            y = value.toDouble()
        }
    var posZ: Int
        get() = z.toInt()
        set(value) {
            z = value.toDouble()
        }
    
}

class Location(var world: World, x: Double, y: Double, z: Double) : Vec3 by Vec3Impl(x, y, z)