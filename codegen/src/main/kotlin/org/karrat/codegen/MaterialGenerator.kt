/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.codegen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.karrat.codegen.utils.*

@OptIn(ExperimentalSerializationApi::class)
fun generateMaterialClass() {
    val file = Thread.currentThread().contextClassLoader.getResourceAsStream("materials.json")!!
    val elements = Json.decodeFromString<JsonArray>(file.readAllBytes().decodeToString())

    file("server/src/main/kotlin/org/karrat/play/Material.kt") {

        generator("MaterialGenerator.kt")
        source("materials.json")

        import("org.karrat.struct.Identifier")

        + """
        public open class Material(
            public val id: Int, 
            public val displayName: kotlin.String, 
            public val identifier: Identifier, 
            public val stackSize: Int
        ) {
        
            public val variations: MutableList<MaterialVariation> = mutableListOf()
            
            public companion object {

                private val materialRegistry: MutableList<Material> = mutableListOf()
    
                public val materials: List<Material> get() = materialRegistry
    
                public fun fromIdentifier(identifier: Identifier): Material {
                    return materialRegistry.first {
                        it.identifier == identifier
                    }
                }
                
                public fun fromId(id: Int): Material {
                    return materialRegistry.first {
                        it.id == id
                    }
                }
    
                public fun register(material: Material) {
                    materialRegistry += material
                }
    
                internal fun registerMaterials() {         
        """.trimIndent()
        elements.forEach {
            val name = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = name.split('_')
            var formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            if (formattedName == "Netherbrick") formattedName = "NetherBrickIngot"
            + "            register($formattedName)"
        }
        + "        }"
        + ""
        + "    }"
        + ""

        elements.forEach {
            val id = it.jsonObject["id"]!!.jsonPrimitive.content
            val displayName = it.jsonObject["displayName"]!!.jsonPrimitive.content
            val stackSize = it.jsonObject["stackSize"]!!.jsonPrimitive.content



            val name = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = name.split('_')
            var formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            if (formattedName == "Netherbrick") formattedName = "NetherBrickIngot"
            indent {
                + "public object ${formattedName.replace(" ", "")} : Material("
                indent {
                    + "id = $id,"
                    + "displayName = \"$displayName\","
                    + "identifier = Identifier(\"minecraft:$name\"),"
                    + "stackSize = $stackSize"
                }

                val variations = it.jsonObject["variations"]?.jsonArray

                if (variations != null) {
                    + ") {"
                    indent {
                        + "init {"
                        variations.forEach { variation ->
                            val metadata = variation.jsonObject["metadata"]!!.jsonPrimitive.content
                            val variationName = variation.jsonObject["displayName"]!!.jsonPrimitive.content

                            + "    variations.add(MaterialVariation($metadata, \"$variationName\"))"
                        }
                        + "}"

                    }
                    + "}"
                } else {
                    + ")"
                }
                + ""
            }
        }
        + """
        }
        
        public data class MaterialVariation(val metadata: Int, val displayName: String)
        """.trimIndent()
    }
}