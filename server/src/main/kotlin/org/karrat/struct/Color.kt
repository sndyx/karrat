/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.struct

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Color(private val color: Int) {

    public constructor(r: Int, g: Int, b: Int) : this(r shl 16 + g shl 8 + b)

    public val r: Int get() = color shr 16 and 0xff
    public val g: Int get() = color shr 8 and 0xff
    public val b: Int get() = color and 0xff

    public companion object Defaults {

        public val white: Color = Color(0xffffff)
        public val black: Color = Color(0x000000)
        public val red: Color = Color(0xff5555)

    }

    override fun toString(): String = color.toString(16).padStart(6, '0')

    public fun toInt(): Int = color

}