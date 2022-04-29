/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import org.karrat.World
import kotlin.math.acos
import kotlin.math.sqrt

public data class Vec3i(
    public var x: Int,
    public var y: Int,
    public var z: Int
) {

    public operator fun plus(other: Vec3i): Vec3i =
        Vec3i(x + other.x, y + other.y, z + other.z)

    public operator fun minus(other: Vec3i): Vec3i =
        Vec3i(x - other.x, y - other.y, z - other.z)

    public operator fun times(other: Int): Vec3i =
        Vec3i(x * other, y * other, z * other)
    
    public operator fun times(other: Double): Vec3d =
        Vec3d(x * other, y * other, z * other)

    public operator fun div(other: Double): Vec3d =
        Vec3d(x / other, y / other, z / other)

}

public data class Vec3d(
    public var x: Double,
    public var y: Double,
    public var z: Double
) {

    public operator fun plus(other: Vec3d): Vec3d =
        Vec3d(x + other.x, y + other.y, z + other.z)

    public operator fun plus(other: Vec3i): Vec3d =
        Vec3d(x + other.x, y + other.y, z + other.z)

    public operator fun minus(other: Vec3d): Vec3d =
        Vec3d(x - other.x, y - other.y, z - other.z)
    
    public operator fun times(other: Double): Vec3d =
        Vec3d(x * other, y * other, z * other)

    public operator fun div(other: Double): Vec3d =
        Vec3d(x / other, y / other, z / other)

}

public data class Location(
    public var world: World,
    public var x: Double,
    public var y: Double,
    public var z: Double
) {
    
    public operator fun plus(other: Vec3i): Location =
        Location(world, x + other.x, y + other.y, z + other.z)

    public operator fun plus(other: Vec3d): Location =
        Location(world, x + other.x, y + other.y, z + other.z)
    
    public operator fun minus(other: Vec3d): Location =
        Location(world, x - other.x, y - other.y, z - other.z)

    public operator fun times(other: Double): Location =
        Location(world, x * other, y * other, z * other)

    public operator fun div(other: Double): Location =
        Location(world, x / other, y / other, z / other)
}

public fun Vec3i.toVec3d(): Vec3d =
    Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

public fun Vec3d.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

public fun Location.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

public fun Location.toVec3d(): Vec3d =
    Vec3d(x, y, z)
    
public fun Vec3d.floor(): Vec3d =
    Vec3d(kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))
    
public fun Location.floor(): Location =
    Location(world, kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))

public fun Vec3d.ceil(): Vec3d =
    Vec3d(kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

public fun Location.ceil(): Location =
    Location(world, kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

public fun Vec3d.normalized(): Vec3d =
    div(mag())

public fun Location.normalized(): Location =
    div(mag())

public fun Vec3i.normalized(): Vec3d =
    div(mag())

public fun Location.mag(): Double =
    sqrt(x * x + y * y + z * z)

public fun Vec3d.mag(): Double =
    sqrt(x * x + y * y + z * z)

public fun Vec3i.mag(): Double =
    sqrt((x * x + y * y + z * z).toDouble())

public fun Location.magXZ(): Double =
    sqrt(x * x + z * z)

public fun Vec3d.magXZ(): Double =
    sqrt(x * x + z * z)

public fun Vec3i.magXZ(): Double =
    sqrt((x * x + z * z).toDouble())

public fun Vec3d.centered(deltaY: Double): Vec3d =
    floor().apply { x += 0.5; y += deltaY; z += 0.5 }
    
public fun Vec3d.centered(): Vec3d =
    centered(0.5)

public fun Vec3d.centeredXZ(): Vec3d =
    centered(0.0)
    
public fun Location.centered(deltaY: Double): Location =
    floor().apply { x += 0.5; y += deltaY; z += 0.5 }
    
public fun Location.centered(): Location =
    centered(0.5)

public fun Location.centeredXZ(): Location =
    centered(0.0)
    
public fun Vec3i.dotProduct(other: Vec3i): Vec3i =
    Vec3i(x * other.x, y * other.y, z * other.z)
    
public fun Vec3i.crossProduct(other: Vec3i): Vec3i =
    Vec3i(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    
public fun Vec3d.dotProduct(other: Vec3d): Vec3d =
    Vec3d(x * other.x, y * other.y, z * other.z)
    
public fun Vec3d.crossProduct(other: Vec3d): Vec3d =
    Vec3d(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    
public fun Vec3i.squareDistanceTo(other: Vec3i): Int =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
public fun Vec3i.distanceTo(other: Vec3i): Double =
    sqrt(distanceTo(other))
    
public fun Vec3d.squareDistanceTo(other: Vec3d): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
public fun Vec3d.distanceTo(other: Vec3d): Double =
    sqrt(squareDistanceTo(other))
 
public fun Location.squareDistanceTo(other: Location): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
public fun Location.distanceTo(other: Location): Double =
    sqrt(squareDistanceTo(other))

// TODO use actual clockwise or counterclockwise angle between formulas
public fun Vec3d.angleFrom(other: Vec3d): Double =
    acos(dotProduct(other).mag() / other.mag() / mag())

public fun Vec3d.angleFrom(other: Vec3i): Double =
    acos(dotProduct(other.toVec3d()).mag() / other.mag() / mag())

public fun Vec3i.angleFrom(other: Vec3i): Double =
    acos(dotProduct(other).mag() / other.mag() / mag())

public fun Vec3i.angleFrom(other: Vec3d): Double =
    other.angleFrom(this)

