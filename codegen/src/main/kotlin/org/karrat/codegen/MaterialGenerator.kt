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
        credit("Prismarine.js")

        import("kotlinx.serialization.Serializable")
        import("org.karrat.struct.Identifier")
        import("org.karrat.struct.id")

        + """
        @Serializable
        public class Material(public val id: Int, public val displayName: String, public val identifier: Identifier, public val stackSize: Int) {
        
            public companion object {
        """.trimIndent()

        elements.forEach {
            val id = it.jsonObject["id"]!!.jsonPrimitive.content
            val displayName = it.jsonObject["displayName"]!!.jsonPrimitive.content
            val stackSize = it.jsonObject["stackSize"]!!.jsonPrimitive.content

            val name = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = name.split('_')
            val formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            + "        public val $formattedName: Material = Material($id, \"$displayName\", id(\"minecraft:$name\"), $stackSize)"
        }

        + """
            }
        }        
        """.trimIndent()
    }
}