/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.world

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.karrat.serialization.nbt.Nbt
import org.karrat.server.Registry
import org.karrat.struct.Identifier
import org.karrat.struct.NbtCompound
import org.karrat.struct.id
import kotlin.io.path.Path
import kotlin.io.path.writeBytes

@Serializable
public open class Dimension(
    @Transient
    public val id: Identifier = id("minecraft:null"),
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
    public val hasCeiling: Boolean,
) {

    public companion object : Registry<Dimension> {

        public val codec: NbtCompound = NbtCompound()
        override val list: MutableList<Dimension> = mutableListOf()

        public fun fromId(id: Int): Dimension = list[id]

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
            val dimensionNbt = NbtCompound()
            dimensionNbt["type"] = "minecraft:dimension_type"
            dimensionNbt["value"] = list.mapIndexed { index, value ->
                val nbt = NbtCompound()
                nbt["name"] = value.id.toString()
                nbt["id"] = index
                nbt["element"] = Nbt.encodeToNbt(value)
                nbt
            }
            val biomeNbt = NbtCompound()
            biomeNbt["type"] = "minecraft:worldgen/biome"
            biomeNbt["value"] = Biome.list.mapIndexed { index, value ->
                val nbt = NbtCompound()
                nbt["name"] = value.id.toString()
                nbt["id"] = index
                nbt["element"] = Nbt.encodeToNbt(value)
                nbt
            }
            codec["minecraft:dimension_type"] = dimensionNbt
            codec["minecraft:worldgen/biome"] = biomeNbt
            Path("new_codec.nbt").writeBytes(Nbt.encodeToBytes(codec))
        }

    }
    
    @Serializable
    public object Overworld : Dimension(
        id = id("minecraft:overworld"),
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
    )
    
    @Serializable
    public object OverworldCaves : Dimension(
        id = id("minecraft:overworld_caves"),
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
    )
    
    @Serializable
    public object Nether : Dimension(
        id = id("minecraft:the_nether"),
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
    )
    
    @Serializable
    public object End : Dimension(
        id = id("minecraft:the_end"),
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
    )
    
}