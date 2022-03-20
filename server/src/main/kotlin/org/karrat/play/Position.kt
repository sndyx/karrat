/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

import org.karrat.World

public interface Vec3 {

    public var x: Double
    public var y: Double
    public var z: Double

}

public fun Vec3(x: Double, y: Double, z: Double): Vec3 =
    Vec3Impl(x, y, z)

internal open class Vec3Impl(
    override var x: Double,
    override var y: Double,
    override var z: Double
) : Vec3 {

    constructor(other: Vec3) : this(other.x, other.y, other.z)

}

public class BlockPos(
    x: Int,
    y: Int,
    z: Int
) : Vec3 by Vec3Impl(x.toDouble(), y.toDouble(), z.toDouble()) {

    public var posX: Int
        get() = x.toInt()
        set(value) {
            x = value.toDouble()
        }
    public var posY: Int
        get() = y.toInt()
        set(value) {
            y = value.toDouble()
        }
    public var posZ: Int
        get() = z.toInt()
        set(value) {
            z = value.toDouble()
        }

}

public class Location(
    public var world: World,
    x: Double,
    y: Double,
    z: Double
) : Vec3 by Vec3Impl(x, y, z)