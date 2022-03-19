package org.karrat.codegen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.karrat.codegen.utils.*

@OptIn(ExperimentalSerializationApi::class)
fun generateMaterialClass() {
    // TODO some way of automating pkg and source specification
    val file = Thread.currentThread().contextClassLoader.getResourceAsStream("materials.json")!!
    val json = Json.decodeFromString<JsonObject>(file.readAllBytes().decodeToString())
    val elements = json["materials"]!!.jsonArray

    file("server/src/main/kotlin/org/karrat/play/Material.kt") {

        generator("MaterialGenerator.kt")
        source("materials.json")

        pkg("org.karrat.play")

        import("kotlinx.serialization.Serializable")
        import("org.karrat.struct.Identifier")
        import("org.karrat.struct.id")

        + """
        @JvmInline
        @Serializable
        public value class Material(public val identifier: Identifier) {
        
            public companion object {
        """.trimIndent()

        elements.forEach {
            val id = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = id.split(':')[1].split('_')
            val formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            + "        public val $formattedName: Material = Material(id(\"$id\"))"
        }

        + """
            }
        }        
        """.trimIndent()
    }
}