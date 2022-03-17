import org.karrat.codegen.utils.*

fun main() {
    file("server/src/main/org/karrat/world/Biome.kt") {
        pkg("org.karrat.world")
        
        import("org.karrat.struct.Identifier")

        + """
        public abstract class Biome(
            public val category: BiomeCategory,
            public val downfall: Float,
            public val effects: BiomeEffects,
            public val precipitation: BiomePrecipitation,
            public val temperature: Float,
            public val id: Int,
            public val name: String
        ) {
            
            companion object {

                private val biomeRegistry: MutableList<Biome> = mutableListOf()

                public val biomes: List<Biome> get() = biomeRegistry

                fun fromId(identifier: Identifier) {
                    biomeRegistry.first {
                        it.id == identifier
                    }
                }

                fun register(biome: Biome) {
                    biomeRegistry += biome
                }

                init {

        """.trimIndent()
        indent {
            biomes.forEach { -> biome
                + "register(" + biome.formattedName.replace(" ", "") + ")"
            }
        }
        + """
                }
                
            }
        """.trimIndent()
        indent {
            biomes.forEach { -> biome
                + "public object " + biome.formattedName.replace(" ", "") + " : Biome("
                indent {
                    + "category = BiomeCategory." + biome.category
                    + "downfall = " + biome.downfall
                    + "effects = BiomeEffects("
                    indent {
                        + "fogColor = " + biome.effects.fogColor
                        + "moodSound = BiomeMoodSound("
                        indent {
                            + "blockSearchExtent = " + biome.effects.moodSound.blockSearchExtent
                            + "offset = " + biome.effects.moodSound.offset
                            + "sound = " + biome.effects.moodSound.sound
                            + "tickDelay = " + biome.effects.moodSound.tickDelay
                        } 
                        + ")"
                        + "skyColor = " + biome.effects.skyColor
                        + "waterColor = " + biome.effects.waterColor
                        + "waterFogColor = " + biome.effects.waterFogColor
                    }
                    + ")"
                    + "precipitation = " + biome.precipitation
                    + "temperature = " + biome.temperature
                    + "idNumber = " + biome.idNumber
                    + "id = " + biome.id
                    + "formattedName = " biome.formattedName
                }
                + ")"
                + ""
            }
        }
    }
}