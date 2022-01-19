/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.world

import kotlinx.serialization.SerialName
import org.karrat.struct.Identifier

data class DimensionType(
    @SerialName("piglin_safe")
    val piglinSafe: Boolean,
    val natural: Boolean,
    @SerialName("ambient_light")
    val ambientLight: Float,
    val infiniburn: Identifier,
    @SerialName("respawn_anchor_works")
    val respawnAnchorWorks: Boolean,
    @SerialName("has_skylight")
    val hasSkylight: Boolean,
    @SerialName("bed_works")
    val bedWorks: Boolean,
    val effects: Identifier,
    @SerialName("has_raids")
    val hasRaids: Boolean,
    @SerialName("min_y")
    val minY: Boolean,
    val height: Int,
    @SerialName("logical_height")
    val logicalHeight: Int,
    @SerialName("coordinate_scale")
    val coordinateScale: Double,
    val ultrawarm: Boolean,
    @SerialName("has_ceiling")
    val hasCeiling: Boolean
)