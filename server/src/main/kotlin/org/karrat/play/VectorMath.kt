/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

import kotlin.math.sqrt

public fun Vec2d.normalized(): Vec2d =
    Vec2d(x.toInt().toDouble(), z.toInt().toDouble())

public fun Vec3d.normalized(): Vec3d =
    Vec3d(x.toInt().toDouble(), y.toInt().toDouble(), z.toInt().toDouble())

public fun Vec2i.centered(): Vec2d =
    Vec2d(x.toDouble() + 0.5, z.toDouble() + 0.5)

public fun Vec3i.centered(centerY: Boolean = false): Vec3d =
    centered(if (centerY) 0.5 else 0.0)

public fun Vec3i.centered(deltaY: Double): Vec3d =
    Vec3d(x.toDouble() + 0.5, y.toDouble() + deltaY, z.toDouble() + 0.5)

@JvmName(name = "vec2RelativeTo")
public fun <T : Vec2> T.relativeTo(other: T): T =
    map {
        xValue = xValue.toDouble() - other.xValue.toDouble()
        zValue = zValue.toDouble() - other.zValue.toDouble()
    }

@JvmName(name = "vec3RelativeTo")
public fun <T : Vec3> T.relativeTo(other: T): T =
    map {
        xValue = xValue.toDouble() - other.xValue.toDouble()
        yValue = yValue.toDouble() - other.yValue.toDouble()
        zValue = zValue.toDouble() - other.zValue.toDouble()
    }

public fun Vec2.dotProduct(other: Vec2): Double =
    xValue.toDouble() * other.xValue.toDouble() +
            zValue.toDouble() * other.zValue.toDouble()

public fun Vec3.dotProduct(other: Vec3): Double =
    xValue.toDouble() * other.xValue.toDouble() +
            yValue.toDouble() * other.yValue.toDouble() +
            zValue.toDouble() * other.zValue.toDouble()

public fun <T : Vec3> T.crossProduct(other: T): T =
    map {
        xValue = yValue.toDouble() * other.zValue.toDouble() - zValue.toDouble() * other.yValue.toDouble()
        yValue = zValue.toDouble() * other.xValue.toDouble() - xValue.toDouble() * other.zValue.toDouble()
        zValue = xValue.toDouble() * other.yValue.toDouble() - yValue.toDouble() * other.xValue.toDouble()
    }

@JvmName(name = "vec2Plus")
public fun <T : Vec2> T.plus(other: T): T =
    map {
        xValue = xValue.toDouble() + other.xValue.toDouble()
        zValue = zValue.toDouble() + other.zValue.toDouble()
    }

@JvmName(name = "vec3Plus")
public fun <T : Vec3> T.plus(other: T): T =
    map {
        xValue = xValue.toDouble() + other.xValue.toDouble()
        yValue = yValue.toDouble() + other.yValue.toDouble()
        zValue = zValue.toDouble() + other.zValue.toDouble()
    }

@JvmName(name = "vec2Minus")
public fun <T : Vec2> T.minus(other: T): T =
    map {
        xValue = xValue.toDouble() - other.xValue.toDouble()
        zValue = zValue.toDouble() - other.zValue.toDouble()
    }

@JvmName(name = "vec3Minus")
public fun <T : Vec3> T.minus(other: T): T =
    map {
        xValue = xValue.toDouble() - other.xValue.toDouble()
        yValue = yValue.toDouble() - other.yValue.toDouble()
        zValue = zValue.toDouble() - other.zValue.toDouble()
    }

public fun Vec2.distanceTo(other: Vec3): Double {
    val d: Double = other.xValue.toDouble() - xValue.toDouble()
    val f: Double = other.zValue.toDouble() - zValue.toDouble()
    return sqrt(d * d + f * f)
}

public fun Vec2.squaredDistanceTo(other: Vec3): Double {
    val d: Double = other.xValue.toDouble() - xValue.toDouble()
    val f: Double = other.zValue.toDouble() - zValue.toDouble()
    return d * d + f * f
}

public fun Vec3.distanceTo(other: Vec3): Double {
    val d: Double = other.xValue.toDouble() - xValue.toDouble()
    val e: Double = other.yValue.toDouble() - yValue.toDouble()
    val f: Double = other.zValue.toDouble() - zValue.toDouble()
    return sqrt(d * d + e * e + f * f)
}

public fun Vec3.squaredDistanceTo(other: Vec3): Double {
    val d: Double = other.xValue.toDouble() - xValue.toDouble()
    val e: Double = other.yValue.toDouble() - yValue.toDouble()
    val f: Double = other.zValue.toDouble() - zValue.toDouble()
    return d * d + e * e + f * f
}