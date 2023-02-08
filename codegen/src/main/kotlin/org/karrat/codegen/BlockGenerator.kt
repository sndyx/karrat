/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.codegen

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import org.karrat.codegen.utils.*

@OptIn(ExperimentalSerializationApi::class)
fun generateBlockClass() {
    val file = Thread.currentThread().contextClassLoader.getResourceAsStream("blocks.json")!!
    val elements = Json.decodeFromString<JsonArray>(file.readAllBytes().decodeToString())

    file("server/src/main/kotlin/org/karrat/world/Block.kt") {

        generator("BlockGenerator.kt")
        source("blocks.json")

        import("kotlinx.serialization.Serializable")
        import("kotlinx.serialization.Transient")
        import("kotlinx.serialization.KSerializer")
        import("org.karrat.struct.Identifier")
        import("org.karrat.struct.Identified")
        import("org.karrat.server.Registry")

        + """
        @Serializable
        public open class Block(
            override val id: Identifier,
            public val ordinal: Int,
            public val displayName: String,
            public val hardness: Float,
            public val resistance: Float,
            public val diggable: Boolean,
            public val transparent: Boolean,
            public val filterLight: Int,
            public val emitLight: Int,
            public val boundingBox: String,
            public val category: String
        ) : Identified {
           
          """.trimIndent()
        indent {
            elements.forEach { e ->
                val id = "minecraft:" + e.jsonObject["name"]!!.jsonPrimitive.content
                val ordinal = e.jsonObject["id"]!!.jsonPrimitive.int
                val displayName = e.jsonObject["displayName"]!!.jsonPrimitive.toString()
                val hardness = e.jsonObject["hardness"]!!.jsonPrimitive.floatOrNull ?: 0.0f
                val resistance = e.jsonObject["resistance"]!!.jsonPrimitive.float
                val diggable = e.jsonObject["diggable"]!!.jsonPrimitive.boolean
                val transparent = e.jsonObject["transparent"]!!.jsonPrimitive.boolean
                val filterLight = e.jsonObject["filterLight"]!!.jsonPrimitive.int
                val emitLight = e.jsonObject["emitLight"]!!.jsonPrimitive.int
                val boundingBox = e.jsonObject["boundingBox"]!!.jsonPrimitive.toString()
                val category = e.jsonObject["material"]!!.jsonPrimitive.toString()

                val states = e.jsonObject["states"]!!.jsonArray.map {
                    val obj = it.jsonObject
                    val name = obj["name"]!!.jsonPrimitive.content
                    val t = when (obj["type"]!!.jsonPrimitive.content) {
                        "int" -> "Int"
                        "bool" -> "Boolean"
                        "enum" -> "String"
                        else -> ""
                    }
                    Pair(name, t)
                }

                val nameParts = id.split(':')[1].split('_')
                val formattedName =
                    nameParts.joinToString(" ") { part -> part.replaceFirstChar { char -> char.uppercaseChar() } }

                if (states.isEmpty()) {
                    +"@Serializable"
                    +"public class ${formattedName.replace(" ", "")} : Block("
                } else {
                    +"@Serializable"
                    +"public class ${formattedName.replace(" ", "")}("
                    indent {
                        states.forEach { state ->
                            +"public var ${state.first}: ${state.second},"
                        }
                    }
                    +") : Block("
                }
                indent {
                    + "id = Identifier(\"$id\"),"
                    + "ordinal = $ordinal,"
                    + "displayName = $displayName,"
                    + "hardness = ${hardness}f,"
                    + "resistance = ${resistance}f,"
                    + "diggable = $diggable,"
                    + "transparent = $transparent,"
                    + "filterLight = $filterLight,"
                    + "emitLight = $emitLight,"
                    + "boundingBox = $boundingBox,"
                    + "category = $category"
                }
                + ") {"
                indent {
                    + "override fun toString(): String = \"$id${states.map { "${it.first}=\$${it.first}" }.joinToString(",").takeIf { it.isNotEmpty() }?.let {"[$it]"} ?: ""}\""
                }
                + "}"
                + ""
            }
        }
        +"}"
    }
}