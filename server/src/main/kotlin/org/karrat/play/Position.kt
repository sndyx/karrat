/*
 * Copyright © Karrat - 2022.
 */
@file:Suppress("Unchecked_Cast")

package org.karrat.play

import org.karrat.World
import kotlinx.serialization.Serializable
import org.karrat.internal.unreachable
import org.karrat.serialization.serializer.*
import kotlin.reflect.KProperty

public open class Vec2(
    internal var xValue: Number,
    internal var zValue: Number
) {
    
    public fun toVec2i(): Vec2i =
        Vec2i(xValue.toInt(), zValue.toInt())
    
    public fun toVec2d(): Vec2d =
        Vec2d(xValue.toDouble(), zValue.toDouble())
    
    public open fun <T : Vec2> map(transform: T.() -> Unit): T =
        (this as T).apply(transform).run { Vec2(xValue, zValue) as T }
    
    public inner class Delegate<T : Number>(private val get: () -> T, private val set: (T) -> Unit) {
    
        public operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
    
        public operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T): Unit = set(value)
        
    }
    
    internal inline fun <reified T : Number> dx(): Delegate<T> = Delegate(
        get = { xValue as? T ?: convert<T>(xValue).also { xValue = it } },
        set = { xValue = it }
    )
    
    internal inline fun <reified T : Number> dz(): Delegate<T> = Delegate(
        get = { zValue as? T ?: convert<T>(zValue).also { zValue = it } },
        set = { zValue = it }
    )
    
    internal inline fun <reified T : Number> convert(value: Number): T {
        return when (T::class) {
            Int::class -> value.toInt() as T
            Double::class -> value.toDouble() as T
            else -> unreachable()
        }
    }
    
}

public open class Vec3(
    xValue: Number,
    internal var yValue: Number,
    zValue: Number
) : Vec2(xValue, zValue) {
    
    internal inline fun <reified T : Number> dy(): Delegate<T> = Delegate(
        get = { yValue as? T ?: convert<T>(yValue).also { yValue = it } },
        set = { yValue = it }
    )
    
    public fun toVec3i(): Vec3i =
        Vec3i(xValue.toInt(), yValue.toInt(), zValue.toInt())
    
    public fun toVec3d(): Vec3d =
        Vec3d(xValue.toDouble(), yValue.toDouble(), zValue.toDouble())
    
    public fun toLocation(world: World): Location =
        Location(world, xValue.toDouble(), yValue.toDouble(), zValue.toDouble())
    
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        (this as T).apply(transform).run { Vec3(xValue, yValue, zValue) as T }
    
}

@Serializable(with = Vec2dSerializer::class)
public class Vec2d(
    x: Double, z: Double
) : Vec2(x, z) {
    public var x: Double by dx()
    public var z: Double by dz()
    
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        (this as T).apply(transform).run { Vec2d(xValue.toDouble(), zValue.toDouble()) as T }
}

@Serializable(with = Vec3dSerializer::class)
public open class Vec3d(
    x: Double,
    y: Double,
    z: Double
): Vec3(x, y, z) {
    public var x: Double by dx()
    public var y: Double by dy()
    public var z: Double by dz()
    
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        ((this as T).apply(transform) as Vec3).run { Vec3d(xValue.toDouble(), yValue.toDouble(), zValue.toDouble()) as T }
}

@Serializable(with = LocationSerializer::class)
public open class Location(
    public var world: World,
    x: Double,
    y: Double,
    z: Double
) : Vec3d(x, y, z) {
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        ((this as T).apply(transform) as Location).run { Location(world, xValue.toDouble(), yValue.toDouble(), zValue.toDouble()) as T }
}

@Serializable(with = Vec2iSerializer::class)
public class Vec2i(
    x: Int,
    z: Int
): Vec2(x, z) {
    public var x: Int by dx()
    public var z: Int by dz()
    
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        (this as T).apply(transform).run { Vec2i(xValue.toInt(), zValue.toInt()) as T }
}

@Serializable(with = Vec3iSerializer::class)
public class Vec3i(
    x: Int,
    y: Int,
    z: Int
): Vec3(x, y, z) {
    public var x: Int by dx()
    public var y: Int by dy()
    public var z: Int by dz()
    
    override fun <T : Vec2> map(transform: T.() -> Unit): T =
        ((this as T).apply(transform) as Vec3).run { Vec3i(xValue.toInt(), yValue.toInt(), zValue.toInt()) as T }
}

public typealias BlockPos = Vec3i