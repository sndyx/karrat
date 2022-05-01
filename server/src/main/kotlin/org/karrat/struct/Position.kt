/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable
import org.karrat.World
import kotlin.math.acos
import kotlin.math.sqrt

@Serializable
public data class Vec2i(
    public var x: Int,
    public var z: Int
) {
    
    public operator fun plus(other: Vec2i): Vec2i =
        Vec2i(x + other.x,  z + other.z)
    
    public operator fun minus(other: Vec2i): Vec2i =
        Vec2i(x - other.x,  z - other.z)
    
    public operator fun times(other: Int): Vec2i =
        Vec2i(x * other,  z * other)
    
    public operator fun div(other: Int): Vec2i =
        Vec2i(x / other,  z / other)
    
}

/**
 * A three-dimensional vector represented in integer values.
 *
 * @see Vec3d
 */
@Serializable
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

/**
 * A three-dimensional vector represented in double-precision floating point
 * numbers.
 *
 * @see Vec3i
 */
@Serializable
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

/**
 * Represents a position within a [given world][world].
 *
 * @see Vec3d
 */
@Serializable
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

public typealias BlockPos = Vec3i

/**
 * Turns this [Vec3i] into a [Vec3d]. The resulting `Vec3d` represents the same
 * vector value as this `Vec3i`.
 */
public fun Vec3i.toVec3d(): Vec3d =
    Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

/**
 * Turns this [Vec3d] into a [Vec3i]. The resulting `Vec3i` represents the same
 * vector value as this `Vec3d`.
 */
public fun Vec3d.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

/**
 * Turns this [Location] into a [Vec3i]. The resulting `Vec3i` represents the
 * same positional value as this `Location`.
 */
public fun Location.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

/**
 * Turns this [Location] into a [Vec3d]. The resulting `Vec3d` represents the
 * same positional value as this `Location`.
 */
public fun Location.toVec3d(): Vec3d =
    Vec3d(x, y, z)

/**
 * Returns a [Vec3d] with all the values of this `Vec3d` rounded towards
 * negative infinity.
 */
public fun Vec3d.floor(): Vec3d =
    Vec3d(kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))

/**
 * Returns a [Location] with all the values of this `Location` rounded towards
 * negative infinity.
 */
public fun Location.floor(): Location =
    Location(world, kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))

/**
 * Returns a [Vec3d] with all the values of this `Vec3d` rounded towards
 * positive infinity.
 */
public fun Vec3d.ceil(): Vec3d =
    Vec3d(kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

/**
 * Returns a [Location] with all the values of this `Location` rounded towards
 * positive infinity.
 */
public fun Location.ceil(): Location =
    Location(world, kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

/**
 * Returns a [Vec3d] with the same direction but a magnitude of one.
 */
public fun Vec3d.normalized(): Vec3d =
    div(magnitude())

/**
 * Returns a [Vec3i] with the same direction but a magnitude of one.
 */
public fun Vec3i.normalized(): Vec3d =
    div(magnitude())

/**
 * Returns the magnitude of this [Vec3d].
 */
public fun Vec3d.magnitude(): Double =
    sqrt(x * x + y * y + z * z)

/**
 * Returns the magnitude of this [Vec3i].
 */
public fun Vec3i.magnitude(): Double =
    sqrt((x * x + y * y + z * z).toDouble())

/**
 * Returns the `xz` magnitude of this [Vec3d].
 */
public fun Vec3d.magnitudeXZ(): Double =
    sqrt(x * x + z * z)

/**
 * Returns the `xz` magnitude of this [Vec3i]
 */
public fun Vec3i.magnitudeXZ(): Double =
    sqrt((x * x + z * z).toDouble())

/**
 * Centers the values of this [Vec3d].
 *
 * @return a `Vec3d` with all the values of this `Vec3d` centered within the
 * two nearest integers.
 *
 * @see centeredXZ
 */
public fun Vec3d.centered(): Vec3d =
    floor().apply { x += 0.5; y += 0.5; z += 0.5 }

/**
 * Centers the `x` and `z` values of this [Vec3d].
 *
 * @return a `Location` with the `x` and `z` values of this `Vec3d` centered within
 * the two nearest integers, and the `y` value floored.
 *
 * @see centered
 */
public fun Vec3d.centeredXZ(): Vec3d =
    floor().apply { x += 0.5; z += 0.5 }

/**
 * Centers the values of this [Location].
 *
 * @return a `Location` with all the values of this `Location` centered within the
 * two nearest integers.
 *
 * @see centeredXZ
 */
public fun Location.centered(): Location =
    floor().apply { x += 0.5; y += 0.5; z += 0.5 }

/**
 * Centers the `x` and `z` values of this [Location].
 *
 * @return a `Location` with the `x` and `z` values of this `Vec3d` centered within
 * the two nearest integers, and the `y` value floored.
 *
 * @see centered
 */
public fun Location.centeredXZ(): Location =
    floor().apply { x += 0.5; z += 0.5 }

/**
 * Returns the dot product of two [vectors][Vec3i].
 */
public fun Vec3i.dotProduct(other: Vec3i): Vec3i =
    Vec3i(x * other.x, y * other.y, z * other.z)

/**
 * Returns the cross product of two [vectors][Vec3i].
 */
public fun Vec3i.crossProduct(other: Vec3i): Vec3i =
    Vec3i(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

/**
 * Returns the dot product of two [vectors][Vec3d].
 */
public fun Vec3d.dotProduct(other: Vec3d): Vec3d =
    Vec3d(x * other.x, y * other.y, z * other.z)

/**
 * Returns the cross product of two [vectors][Vec3d].
 */
public fun Vec3d.crossProduct(other: Vec3d): Vec3d =
    Vec3d(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

/**
 * Returns the square distance to the specified [Vec3i]. Less accurate than
 * [distanceTo] but significantly faster.
 */
public fun Vec3i.squareDistanceTo(other: Vec3i): Double =
    ((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)).toDouble()

/**
 * Returns the Euclidean distance to the specified [Vec3i].
 *
 * @see squareDistanceTo
 */
public fun Vec3i.distanceTo(other: Vec3i): Double =
    sqrt(squareDistanceTo(other))

/**
 * Returns the square distance to the specified [Vec3d]. Less accurate than
 * [distanceTo] but significantly faster.
 */
public fun Vec3d.squareDistanceTo(other: Vec3d): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)

/**
 * Returns the Euclidean distance to the specified [Vec3d].
 *
 * @see squareDistanceTo
 */
public fun Vec3d.distanceTo(other: Vec3d): Double =
    sqrt(squareDistanceTo(other))

/**
 * Returns the square distance to the specified [Location]. Less accurate than
 * [distanceTo] but significantly faster.
 */
public fun Location.squareDistanceTo(other: Location): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)

/**
 * Returns the Euclidean distance to the specified [Location].
 *
 * @see squareDistanceTo
 */
public fun Location.distanceTo(other: Location): Double =
    sqrt(squareDistanceTo(other))

// TODO use actual clockwise or counterclockwise angle between formulas
/**
 * Don't know what this does lmao. TODO: document
 */
public fun Vec3d.angleFrom(other: Vec3d): Double =
    acos(dotProduct(other).magnitude() / other.magnitude() / magnitude())

/**
 * Don't know what this does lmao. TODO: document
 */
public fun Vec3i.angleFrom(other: Vec3i): Double =
    acos(dotProduct(other).magnitude() / other.magnitude() / magnitude())