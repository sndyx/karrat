/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import kotlin.math.sqrt

public data class Vec3i(
    public var x: Int,
    public var y: Int,
    public var z: Int
) {

    public operator fun plus(other: Vec3i): Vec3i =
        Vec3i(x + other.x, y + other.y, z + other.z)

}

public data class Vec3d(
    public var x: Double,
    public var y: Double,
    public var z: Double
) {

    public operator fun plus(other: Vec3d): Vec3d =
        Vec3d(x + other.x, y + other.y, z + other.z)

}

public data class Location(
    public var world: World,
    public var x: Double,
    public var y: Double,
    public var z: Double
) {

    public operator fun plus(other: Location): Location =
        Location(world, x + other.x, y + other.y, z + other.z)

}

fun Vec3i.toVec3d(): Vec3d =
    Vec3d(x.toDouble(), y.toDouble(), z.toDouble())

fun Vec3d.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

fun Location.toVec3i(): Vec3i =
    Vec3i(x.toInt(), y.toInt(), z.toInt())

fun Location.toVec3d(): Vec3d =
    Vec3d(x, y, z)
    
fun Vec3d.normalized(): Vec3d =
    Vec3d(x.toInt().toDouble(), y.toInt().toDouble(), z.toInt().toDouble())
    
fun Location.normalized(): Location =
    Location(world, x.toInt().toDouble(), y.toInt().toDouble(), z.toInt().toDouble())
    
fun Vec3d.centered(deltaY: Double): Vec3d =
    normalized().apply { x += 0.5; y += deltaY; z += 0.5 }
    
fun Vec3d.centered(centerY: Boolean = true): Vec3d =
    centered(0.5)
    
fun Location.centered(deltaY: Double): Location =
    normalized().apply { x += 0.5; y += deltaY; z += 0.5 }
    
fun Location.centered(centerY: Boolean = true): Location =
    centered(0.5)
    
fun Vec3i.dotProduct(other: Vec3i): Vec3i =
    Vec3i(x * other.x, y * other.y, z * other.z)
    
fun Vec3i.crossProduct(other: Vec3i): Vec3i =
    Vec3i(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    
fun Vec3d.dotProduct(other: Vec3d): Vec3d =
    Vec3d(x * other.x, y * other.y, z * other.z)
    
fun Vec3d.crossProduct(other: Vec3d): Vec3d =
    Vec3d(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    
fun Location.dotProduct(other: Location): Location =
    Location(world, x * other.x, y * other.y, z * other.z)
    
fun Location.crossProduct(other: Location): Location =
    Location(
        world,
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )
    
fun Vec3i.squareDistanceTo(other: Vec3i): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
fun Vec3i.distanceTo(other: Vec3i): Double =
    sqrt(distanceTo(other))
    
fun Vec3d.squareDistanceTo(other: Vec3d): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
fun Vec3d.distanceTo(other: Vec3d): Double =
    sqrt(distanceTo(other))
 
fun Location.squareDistanceTo(other: Location): Double =
    (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)
    
fun Location.distanceTo(other: Location): Double =
    sqrt(distanceTo(other))
