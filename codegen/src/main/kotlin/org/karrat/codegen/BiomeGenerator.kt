/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.codegen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.karrat.codegen.utils.*

@OptIn(ExperimentalSerializationApi::class)
fun generateBiomeClass() {
    val file = Thread.currentThread().contextClassLoader.getResourceAsStream("dimension_codec.json")!!
    val json = Json.decodeFromString<JsonObject>(file.readAllBytes().decodeToString())
    val elements = json["minecraft:worldgen/biome"]!!.jsonObject["value"]!!.jsonArray

    val precipitationTypes = mutableSetOf<String>()
    val biomeCategories = mutableSetOf<String>()

    file("server/src/main/kotlin/org/karrat/world/Biome.kt") {

        generator("BiomeGenerator.kt")
        source("dimension_codec.json")
    
        import("kotlinx.serialization.Serializable")
        import("kotlinx.serialization.Transient")
        import("kotlinx.serialization.KSerializer")
        import("org.karrat.struct.Identifier")
        import("org.karrat.struct.Identified")
        import("org.karrat.struct.Codec")

        + """
        @Serializable
        public open class Biome(
            public val category: BiomeCategory,
            public val downfall: Float,
            public val effects: BiomeEffects,
            public val precipitation: BiomePrecipitation,
            public val temperature: Float,
            @Transient
            override val id: Identifier = Identifier("minecraft:null"),
            public val ordinal: Int,
            @Transient
            public val name: String = ""
        ) : Identified {
            
            public companion object BiomeRegistry : Codec<Biome>() {

                override val id: Identifier = Identifier("minecraft:worldgen/biome")
                override val serializer: KSerializer<Biome> = serializer()
                override val list: MutableList<Biome> = mutableListOf()

                public fun fromIdentifier(identifier: Identifier): Biome {
                    return list.first {
                        it.id == identifier
                    }
                }

                override fun register(value: Biome) {
                    list.add(value)
                }

                override fun unregister(value: Biome) {
                    list.remove(value)
                }

                override fun load() {             
        """.trimIndent()
        elements.forEach {
            val id = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = id.split(':')[1].split('_')
            val formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            + "            register($formattedName)"
        }
        + "        }"
        + "    }"
        + ""
        indent {
            elements.forEach {
                val id = it.jsonObject["name"]!!.jsonPrimitive.content
                val ordinal = it.jsonObject["id"]!!.jsonPrimitive.int
                val element = it.jsonObject["element"]!!.jsonObject

                val category = element["category"]!!.jsonPrimitive.content.split("_").joinToString("") { part -> part.replaceFirstChar { char -> char.uppercaseChar() } }
                biomeCategories.add(category)

                val downfall = element["downfall"]!!.jsonPrimitive.float
                val effects = element["effects"]!!.jsonObject
                val fogColor = effects["fog_color"]!!.jsonPrimitive.int
                val moodSound = effects["mood_sound"]!!.jsonObject
                val blockSearchExtent = moodSound["block_search_extent"]!!.jsonPrimitive.int
                val offset = moodSound["offset"]!!.jsonPrimitive.int
                val sound = moodSound["sound"]!!.jsonPrimitive.content
                val tickDelay = moodSound["tick_delay"]!!.jsonPrimitive.long
                val skyColor = effects["sky_color"]!!.jsonPrimitive.int
                val waterColor = effects["water_color"]!!.jsonPrimitive.int
                val waterFogColor = effects["water_fog_color"]!!.jsonPrimitive.int
                val precipitation = element["precipitation"]!!.jsonPrimitive.content.replaceFirstChar { char -> char.uppercaseChar() }
                precipitationTypes.add(precipitation)

                val temperature = element["temperature"]!!.jsonPrimitive.float

                val nameParts = id.split(':')[1].split('_')
                val formattedName =
                    nameParts.joinToString(" ") { part -> part.replaceFirstChar { char -> char.uppercaseChar() } }

                + "@Serializable"
                + "public object ${formattedName.replace(" ", "")} : Biome("
                indent {
                    + "category = BiomeCategory.$category,"
                    + "downfall = ${downfall}f,"
                    + "effects = BiomeEffects("
                    indent {
                        + "fogColor = $fogColor,"
                        + "moodSound = BiomeMoodSound("
                        indent {
                            + "blockSearchExtent = $blockSearchExtent,"
                            + "offset = $offset,"
                            + "sound = Identifier(\"$sound\"),"
                            + "tickDelay = ${tickDelay}L"
                        }
                        + "),"
                        + "skyColor = $skyColor,"
                        + "waterColor = $waterColor,"
                        + "waterFogColor = $waterFogColor"
                    }
                    + "),"
                    + "precipitation = BiomePrecipitation.$precipitation,"
                    + "temperature = ${temperature}f,"
                    + "id = Identifier(\"$id\"),"
                    + "ordinal = $ordinal,"
                    + "name = \"$formattedName\""
                }
                + ")"
                + ""
            }
        }
        + "}"
        + ""
        + "public enum class BiomeCategory {"
        indent {
            + ""
            biomeCategories.forEach {
                + "$it,"
            }
            + ""
        }
        + "}"
        + ""
        + "public enum class BiomePrecipitation {"
        indent {
            + ""
            precipitationTypes.forEach {
                + "$it,"
            }
            + ""
        }
        + """
            }
            
            @Serializable
            public data class BiomeEffects(
                public val fogColor: Int,
                public val skyColor: Int,
                public val waterColor: Int,
                public val waterFogColor: Int,
                public val moodSound: BiomeMoodSound
            )
            
            @Serializable
            public data class BiomeMoodSound(
                public val blockSearchExtent: Int,
                public val offset: Int,
                public val sound: Identifier,
                public val tickDelay: Long
            )
        """.trimIndent()
    }
}