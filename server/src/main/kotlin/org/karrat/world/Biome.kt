/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.world

import org.karrat.struct.Identifier

// Auto-generated file. DO NOT EDIT!

public abstract class Biome(
    public val category: BiomeCategory,
    public val downfall: Float,
    public val effects: BiomeEffects,
    public val precipitation: BiomePrecipitation,
    public val temperature: Float,
    public val id: Identifier,
    public val idNumber: Int,
    public val name: String
) {
    
    public companion object {

        private val biomeRegistry: MutableList<Biome> = mutableListOf()

        public val biomes: List<Biome> get() = biomeRegistry

        public fun fromId(identifier: Identifier) {
            biomeRegistry.first {
                it.id == identifier
            }
        }

        public fun register(biome: Biome) {
            biomeRegistry += biome
        }

        init {             
            register(TheVoid)
            register(Plains)
            register(SunflowerPlains)
            register(SnowyPlains)
            register(IceSpikes)
            register(Desert)
            register(Swamp)
            register(Forest)
            register(FlowerForest)
            register(BirchForest)
            register(DarkForest)
            register(OldGrowthBirchForest)
            register(OldGrowthPineTaiga)
            register(OldGrowthSpruceTaiga)
            register(Taiga)
            register(SnowyTaiga)
            register(Savanna)
            register(SavannaPlateau)
            register(WindsweptHills)
            register(WindsweptGravellyHills)
            register(WindsweptForest)
            register(WindsweptSavanna)
            register(Jungle)
            register(SparseJungle)
            register(BambooJungle)
            register(Badlands)
            register(ErodedBadlands)
            register(WoodedBadlands)
            register(Meadow)
            register(Grove)
            register(SnowySlopes)
            register(FrozenPeaks)
            register(JaggedPeaks)
            register(StonyPeaks)
            register(River)
            register(FrozenRiver)
            register(Beach)
            register(SnowyBeach)
            register(StonyShore)
            register(WarmOcean)
            register(LukewarmOcean)
            register(DeepLukewarmOcean)
            register(Ocean)
            register(DeepOcean)
            register(ColdOcean)
            register(DeepColdOcean)
            register(FrozenOcean)
            register(DeepFrozenOcean)
            register(MushroomFields)
            register(DripstoneCaves)
            register(LushCaves)
            register(NetherWastes)
            register(WarpedForest)
            register(CrimsonForest)
            register(SoulSandValley)
            register(BasaltDeltas)
            register(TheEnd)
            register(EndHighlands)
            register(EndMidlands)
            register(SmallEndIslands)
            register(EndBarrens)
        }
    }

    public object TheVoid : Biome(
        category = BiomeCategory.None,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:the_void"),
        idNumber = 0,
        name = "The Void"
    )
    
    public object Plains : Biome(
        category = BiomeCategory.Plains,
        downfall = 0.4f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7907327,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.8f,
        id = Identifier("minecraft:plains"),
        idNumber = 1,
        name = "Plains"
    )
    
    public object SunflowerPlains : Biome(
        category = BiomeCategory.Plains,
        downfall = 0.4f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7907327,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.8f,
        id = Identifier("minecraft:sunflower_plains"),
        idNumber = 2,
        name = "Sunflower Plains"
    )
    
    public object SnowyPlains : Biome(
        category = BiomeCategory.Icy,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8364543,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = 0.0f,
        id = Identifier("minecraft:snowy_plains"),
        idNumber = 3,
        name = "Snowy Plains"
    )
    
    public object IceSpikes : Biome(
        category = BiomeCategory.Icy,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8364543,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = 0.0f,
        id = Identifier("minecraft:ice_spikes"),
        idNumber = 4,
        name = "Ice Spikes"
    )
    
    public object Desert : Biome(
        category = BiomeCategory.Desert,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:desert"),
        idNumber = 5,
        name = "Desert"
    )
    
    public object Swamp : Biome(
        category = BiomeCategory.Swamp,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7907327,
            waterColor = 6388580,
            waterFogColor = 2302743
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.8f,
        id = Identifier("minecraft:swamp"),
        idNumber = 6,
        name = "Swamp"
    )
    
    public object Forest : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7972607,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.7f,
        id = Identifier("minecraft:forest"),
        idNumber = 7,
        name = "Forest"
    )
    
    public object FlowerForest : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7972607,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.7f,
        id = Identifier("minecraft:flower_forest"),
        idNumber = 8,
        name = "Flower Forest"
    )
    
    public object BirchForest : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.6f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8037887,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.6f,
        id = Identifier("minecraft:birch_forest"),
        idNumber = 9,
        name = "Birch Forest"
    )
    
    public object DarkForest : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7972607,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.7f,
        id = Identifier("minecraft:dark_forest"),
        idNumber = 10,
        name = "Dark Forest"
    )
    
    public object OldGrowthBirchForest : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.6f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8037887,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.6f,
        id = Identifier("minecraft:old_growth_birch_forest"),
        idNumber = 11,
        name = "Old Growth Birch Forest"
    )
    
    public object OldGrowthPineTaiga : Biome(
        category = BiomeCategory.Taiga,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8168447,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.3f,
        id = Identifier("minecraft:old_growth_pine_taiga"),
        idNumber = 12,
        name = "Old Growth Pine Taiga"
    )
    
    public object OldGrowthSpruceTaiga : Biome(
        category = BiomeCategory.Taiga,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233983,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.25f,
        id = Identifier("minecraft:old_growth_spruce_taiga"),
        idNumber = 13,
        name = "Old Growth Spruce Taiga"
    )
    
    public object Taiga : Biome(
        category = BiomeCategory.Taiga,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233983,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.25f,
        id = Identifier("minecraft:taiga"),
        idNumber = 14,
        name = "Taiga"
    )
    
    public object SnowyTaiga : Biome(
        category = BiomeCategory.Taiga,
        downfall = 0.4f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8625919,
            waterColor = 4020182,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = -0.5f,
        id = Identifier("minecraft:snowy_taiga"),
        idNumber = 15,
        name = "Snowy Taiga"
    )
    
    public object Savanna : Biome(
        category = BiomeCategory.Savanna,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:savanna"),
        idNumber = 16,
        name = "Savanna"
    )
    
    public object SavannaPlateau : Biome(
        category = BiomeCategory.Savanna,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:savanna_plateau"),
        idNumber = 17,
        name = "Savanna Plateau"
    )
    
    public object WindsweptHills : Biome(
        category = BiomeCategory.ExtremeHills,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233727,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.2f,
        id = Identifier("minecraft:windswept_hills"),
        idNumber = 18,
        name = "Windswept Hills"
    )
    
    public object WindsweptGravellyHills : Biome(
        category = BiomeCategory.ExtremeHills,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233727,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.2f,
        id = Identifier("minecraft:windswept_gravelly_hills"),
        idNumber = 19,
        name = "Windswept Gravelly Hills"
    )
    
    public object WindsweptForest : Biome(
        category = BiomeCategory.ExtremeHills,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233727,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.2f,
        id = Identifier("minecraft:windswept_forest"),
        idNumber = 20,
        name = "Windswept Forest"
    )
    
    public object WindsweptSavanna : Biome(
        category = BiomeCategory.Savanna,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:windswept_savanna"),
        idNumber = 21,
        name = "Windswept Savanna"
    )
    
    public object Jungle : Biome(
        category = BiomeCategory.Jungle,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7842047,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.95f,
        id = Identifier("minecraft:jungle"),
        idNumber = 22,
        name = "Jungle"
    )
    
    public object SparseJungle : Biome(
        category = BiomeCategory.Jungle,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7842047,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.95f,
        id = Identifier("minecraft:sparse_jungle"),
        idNumber = 23,
        name = "Sparse Jungle"
    )
    
    public object BambooJungle : Biome(
        category = BiomeCategory.Jungle,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7842047,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.95f,
        id = Identifier("minecraft:bamboo_jungle"),
        idNumber = 24,
        name = "Bamboo Jungle"
    )
    
    public object Badlands : Biome(
        category = BiomeCategory.Mesa,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:badlands"),
        idNumber = 25,
        name = "Badlands"
    )
    
    public object ErodedBadlands : Biome(
        category = BiomeCategory.Mesa,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:eroded_badlands"),
        idNumber = 26,
        name = "Eroded Badlands"
    )
    
    public object WoodedBadlands : Biome(
        category = BiomeCategory.Mesa,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:wooded_badlands"),
        idNumber = 27,
        name = "Wooded Badlands"
    )
    
    public object Meadow : Biome(
        category = BiomeCategory.Mountain,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 937679,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:meadow"),
        idNumber = 28,
        name = "Meadow"
    )
    
    public object Grove : Biome(
        category = BiomeCategory.Forest,
        downfall = 0.8f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8495359,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = -0.2f,
        id = Identifier("minecraft:grove"),
        idNumber = 29,
        name = "Grove"
    )
    
    public object SnowySlopes : Biome(
        category = BiomeCategory.Mountain,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8560639,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = -0.3f,
        id = Identifier("minecraft:snowy_slopes"),
        idNumber = 30,
        name = "Snowy Slopes"
    )
    
    public object FrozenPeaks : Biome(
        category = BiomeCategory.Mountain,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8756735,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = -0.7f,
        id = Identifier("minecraft:frozen_peaks"),
        idNumber = 31,
        name = "Frozen Peaks"
    )
    
    public object JaggedPeaks : Biome(
        category = BiomeCategory.Mountain,
        downfall = 0.9f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8756735,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = -0.7f,
        id = Identifier("minecraft:jagged_peaks"),
        idNumber = 32,
        name = "Jagged Peaks"
    )
    
    public object StonyPeaks : Biome(
        category = BiomeCategory.Mountain,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7776511,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 1.0f,
        id = Identifier("minecraft:stony_peaks"),
        idNumber = 33,
        name = "Stony Peaks"
    )
    
    public object River : Biome(
        category = BiomeCategory.River,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:river"),
        idNumber = 34,
        name = "River"
    )
    
    public object FrozenRiver : Biome(
        category = BiomeCategory.River,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8364543,
            waterColor = 3750089,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = 0.0f,
        id = Identifier("minecraft:frozen_river"),
        idNumber = 35,
        name = "Frozen River"
    )
    
    public object Beach : Biome(
        category = BiomeCategory.Beach,
        downfall = 0.4f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7907327,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.8f,
        id = Identifier("minecraft:beach"),
        idNumber = 36,
        name = "Beach"
    )
    
    public object SnowyBeach : Biome(
        category = BiomeCategory.Beach,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8364543,
            waterColor = 4020182,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = 0.05f,
        id = Identifier("minecraft:snowy_beach"),
        idNumber = 37,
        name = "Snowy Beach"
    )
    
    public object StonyShore : Biome(
        category = BiomeCategory.Beach,
        downfall = 0.3f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8233727,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.2f,
        id = Identifier("minecraft:stony_shore"),
        idNumber = 38,
        name = "Stony Shore"
    )
    
    public object WarmOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4445678,
            waterFogColor = 270131
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:warm_ocean"),
        idNumber = 39,
        name = "Warm Ocean"
    )
    
    public object LukewarmOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4566514,
            waterFogColor = 267827
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:lukewarm_ocean"),
        idNumber = 40,
        name = "Lukewarm Ocean"
    )
    
    public object DeepLukewarmOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4566514,
            waterFogColor = 267827
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:deep_lukewarm_ocean"),
        idNumber = 41,
        name = "Deep Lukewarm Ocean"
    )
    
    public object Ocean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:ocean"),
        idNumber = 42,
        name = "Ocean"
    )
    
    public object DeepOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:deep_ocean"),
        idNumber = 43,
        name = "Deep Ocean"
    )
    
    public object ColdOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4020182,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:cold_ocean"),
        idNumber = 44,
        name = "Cold Ocean"
    )
    
    public object DeepColdOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4020182,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:deep_cold_ocean"),
        idNumber = 45,
        name = "Deep Cold Ocean"
    )
    
    public object FrozenOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8364543,
            waterColor = 3750089,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Snow,
        temperature = 0.0f,
        id = Identifier("minecraft:frozen_ocean"),
        idNumber = 46,
        name = "Frozen Ocean"
    )
    
    public object DeepFrozenOcean : Biome(
        category = BiomeCategory.Ocean,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 3750089,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:deep_frozen_ocean"),
        idNumber = 47,
        name = "Deep Frozen Ocean"
    )
    
    public object MushroomFields : Biome(
        category = BiomeCategory.Mushroom,
        downfall = 1.0f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7842047,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.9f,
        id = Identifier("minecraft:mushroom_fields"),
        idNumber = 48,
        name = "Mushroom Fields"
    )
    
    public object DripstoneCaves : Biome(
        category = BiomeCategory.Underground,
        downfall = 0.4f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 7907327,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.8f,
        id = Identifier("minecraft:dripstone_caves"),
        idNumber = 49,
        name = "Dripstone Caves"
    )
    
    public object LushCaves : Biome(
        category = BiomeCategory.Underground,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 12638463,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 8103167,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.Rain,
        temperature = 0.5f,
        id = Identifier("minecraft:lush_caves"),
        idNumber = 50,
        name = "Lush Caves"
    )
    
    public object NetherWastes : Biome(
        category = BiomeCategory.Nether,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 3344392,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.nether_wastes.mood"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:nether_wastes"),
        idNumber = 51,
        name = "Nether Wastes"
    )
    
    public object WarpedForest : Biome(
        category = BiomeCategory.Nether,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 1705242,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.warped_forest.mood"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:warped_forest"),
        idNumber = 52,
        name = "Warped Forest"
    )
    
    public object CrimsonForest : Biome(
        category = BiomeCategory.Nether,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 3343107,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.crimson_forest.mood"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:crimson_forest"),
        idNumber = 53,
        name = "Crimson Forest"
    )
    
    public object SoulSandValley : Biome(
        category = BiomeCategory.Nether,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 1787717,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.soul_sand_valley.mood"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:soul_sand_valley"),
        idNumber = 54,
        name = "Soul Sand Valley"
    )
    
    public object BasaltDeltas : Biome(
        category = BiomeCategory.Nether,
        downfall = 0.0f,
        effects = BiomeEffects(
            fogColor = 6840176,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.basalt_deltas.mood"),
                tickDelay = 6000L
            ),
            skyColor = 7254527,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 2.0f,
        id = Identifier("minecraft:basalt_deltas"),
        idNumber = 55,
        name = "Basalt Deltas"
    )
    
    public object TheEnd : Biome(
        category = BiomeCategory.TheEnd,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 10518688,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 0,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:the_end"),
        idNumber = 56,
        name = "The End"
    )
    
    public object EndHighlands : Biome(
        category = BiomeCategory.TheEnd,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 10518688,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 0,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:end_highlands"),
        idNumber = 57,
        name = "End Highlands"
    )
    
    public object EndMidlands : Biome(
        category = BiomeCategory.TheEnd,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 10518688,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 0,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:end_midlands"),
        idNumber = 58,
        name = "End Midlands"
    )
    
    public object SmallEndIslands : Biome(
        category = BiomeCategory.TheEnd,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 10518688,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 0,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:small_end_islands"),
        idNumber = 59,
        name = "Small End Islands"
    )
    
    public object EndBarrens : Biome(
        category = BiomeCategory.TheEnd,
        downfall = 0.5f,
        effects = BiomeEffects(
            fogColor = 10518688,
            moodSound = BiomeMoodSound(
                blockSearchExtent = 8,
                offset = 2,
                sound = Identifier("minecraft:ambient.cave"),
                tickDelay = 6000L
            ),
            skyColor = 0,
            waterColor = 4159204,
            waterFogColor = 329011
        ),
        precipitation = BiomePrecipitation.None,
        temperature = 0.5f,
        id = Identifier("minecraft:end_barrens"),
        idNumber = 60,
        name = "End Barrens"
    )
    
}

public enum class BiomeCategory {
    
    None,
    Plains,
    Icy,
    Desert,
    Swamp,
    Forest,
    Taiga,
    Savanna,
    ExtremeHills,
    Jungle,
    Mesa,
    Mountain,
    River,
    Beach,
    Ocean,
    Mushroom,
    Underground,
    Nether,
    TheEnd,
    
}

public enum class BiomePrecipitation {
    
    None,
    Rain,
    Snow,
    
}

public data class BiomeEffects(
    public val fogColor: Int,
    public val skyColor: Int,
    public val waterColor: Int,
    public val waterFogColor: Int,
    public val moodSound: BiomeMoodSound
)

public data class BiomeMoodSound(
    public val blockSearchExtent: Int,
    public val offset: Int,
    public val sound: Identifier,
    public val tickDelay: Long
)
