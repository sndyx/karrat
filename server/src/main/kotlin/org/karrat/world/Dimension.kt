/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.world

import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import org.karrat.server.Loadable
import org.karrat.struct.Identifier
import org.karrat.struct.id

public abstract class Dimension(
    @SerialName("piglin_safe")
    public val piglinSafe: Boolean,
    public val natural: Boolean,
    @SerialName("ambient_light")
    public val ambientLight: Float,
    public val infiniburn: Identifier,
    @SerialName("respawn_anchor_works")
    public val respawnAnchorWorks: Boolean,
    @SerialName("has_skylight")
    public val hasSkylight: Boolean,
    @SerialName("bed_works")
    public val bedWorks: Boolean,
    public val effects: Identifier,
    @SerialName("fixed_time")
    public val fixedTime: Long?,
    @SerialName("has_raids")
    public val hasRaids: Boolean,
    @SerialName("min_y")
    public val minY: Int,
    public val height: Int,
    @SerialName("logical_height")
    public val logicalHeight: Int,
    @SerialName("coordinate_scale")
    public val coordinateScale: Double,
    @SerialName("ultrawarm")
    public val ultraWarm: Boolean,
    @SerialName("has_ceiling")
    public val hasCeiling: Boolean
) {
    
    @Transient
    public abstract val id: Int
    
    public companion object : Loadable<Dimension> {

        override val list: MutableList<Dimension> = mutableListOf()

        public fun fromId(id: Int): Dimension = list.first { it.id == id }

        override fun register(value: Dimension) {
            list.add(value)
        }

        override fun unregister(value: Dimension) {
            list.remove(value)
        }

        override fun load() {
            register(Overworld)
            register(OverworldCaves)
            register(Nether)
            register(End)
        }

    }
    
    public object Overworld : Dimension(
        piglinSafe = false,
        natural = true,
        ambientLight = 0.0f,
        infiniburn = id("minecraft:infiniburn_overworld"),
        respawnAnchorWorks = false,
        hasSkylight = true,
        bedWorks = true,
        effects = id("minecraft:overworld"),
        fixedTime = null,
        hasRaids = true,
        minY = 0,
        height = 256,
        logicalHeight = 256,
        coordinateScale = 1.0,
        ultraWarm = false,
        hasCeiling = false
    ) {
    
        override val id: Int = 0
    
    }
    
    public object OverworldCaves : Dimension(
        piglinSafe = false,
        natural = true,
        ambientLight = 0.0f,
        infiniburn = id("minecraft:infiniburn_overworld"),
        respawnAnchorWorks = false,
        hasSkylight = true,
        bedWorks = true,
        effects = id("minecraft:overworld"),
        fixedTime = null,
        hasRaids = true,
        minY = 0,
        height = 256,
        logicalHeight = 256,
        coordinateScale = 1.0,
        ultraWarm = false,
        hasCeiling = true
    ) {
        
        override val id: Int = 1
        
    }
    
    public object Nether : Dimension(
        piglinSafe = true,
        natural = false,
        ambientLight = 0.1f,
        infiniburn = id("minecraft:infiniburn_nether"),
        respawnAnchorWorks = true,
        hasSkylight = false,
        bedWorks = false,
        effects = id("minecraft:the_nether"),
        fixedTime = 18000L,
        hasRaids = false,
        minY = 0,
        height = 256,
        logicalHeight = 128,
        coordinateScale = 8.0,
        ultraWarm = true,
        hasCeiling = true
    ) {
        
        override val id: Int = 2
        
    }
    
    public object End : Dimension(
        piglinSafe = false,
        natural = false,
        ambientLight = 0.0f,
        infiniburn = id("minecraft:infiniburn_end"),
        respawnAnchorWorks = false,
        hasSkylight = false,
        bedWorks = false,
        effects = id("minecraft:the_end"),
        fixedTime = 6000L,
        hasRaids = true,
        minY = 0,
        height = 256,
        logicalHeight = 256,
        coordinateScale = 1.0,
        ultraWarm = false,
        hasCeiling = false
    ) {
        
        override val id: Int = 3
        
    }
    
}