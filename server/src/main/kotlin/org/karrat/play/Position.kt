/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

import org.karrat.World
import kotlinx.serialization.Serializable

public interface Vec3 {

    public var x: Double
    public var y: Double
    public var z: Double

}

@Serializable
public open class Vec3d(
    override var x: Double,
    override var y: Double,
    override var z: Double
) : Vec3

@Serializable
public class Location(
    public var world: World,
    override var x: Double,
    override var y: Double,
    override var z: Double
) : Vec3

@Serializable
public class BlockPos(
    public var xPos: Int,
    public var yPos: Int,
    public var zPos: Int
) : Vec3 {

    override var x: Double
        get() = xPos.toDouble()
        set(value) {
            xPos = value.toInt()
        }

    override var y: Double
        get() = yPos.toDouble()
        set(value) {
            yPos = value.toInt()
        }

    override var z: Double
        get() = zPos.toDouble()
        set(value) {
            zPos = value.toInt()
        }

}