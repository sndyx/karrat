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

        import("org.karrat.struct`.Loadable")
        import("org.karrat.struct.Identifier")

        + """
        public open class Material(
            public val id: Int, 
            public val displayName: kotlin.String, 
            public val identifier: Identifier, 
            public val stackSize: Int
        ) {
                    
            public companion object MaterialRegistry : Loadable<Material> {

                override val list: MutableList<Material> = mutableListOf()
        
                public fun fromIdentifier(identifier: Identifier): Material {
                    return list.first {
                        it.identifier == identifier
                    }
                }
                
                public fun fromId(id: Int): Material {
                    return list.first {
                        it.id == id
                    }
                }
    
                override fun register(value: Material) {
                    list.add(value)
                }
                
                override fun unregister(value: Material) {
                    list.remove(value)
                }
    
                override fun load() {         
        """.trimIndent()
        elements.forEach {
            val name = it.jsonObject["name"]!!.jsonPrimitive.content
            val nameParts = name.split('_')
            val formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
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
            val formattedName = nameParts.joinToString("") { s -> s.replaceFirstChar { c -> c.uppercaseChar() } }
            indent {
                + "public object ${formattedName.replace(" ", "")} : Material("
                indent {
                    + "id = $id,"
                    + "displayName = \"$displayName\","
                    + "identifier = Identifier(\"minecraft:$name\"),"
                    + "stackSize = $stackSize"
                }
                + ")"
                + ""
            }
        }
        + "}"
    }
}