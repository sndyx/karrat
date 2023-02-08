/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.struct

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.karrat.plugin.Plugin
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
internal open class Person(
    @Transient
    override val id: Identifier = Identifier("minecraft:null"),
    val name: String,
    val age: Int
) : Identified {

    companion object : Codec<Person>() {

        override val id: Identifier = id("test:person_type")
        override val serializer: KSerializer<Person> = serializer()
        override val list: MutableList<Person> = mutableListOf()

        override fun load() {
            list.add(Person1)
            list.add(Person2)
        }

        context(Plugin) override fun unregister(value: Person) {
            TODO("Not yet implemented")
        }

        context(Plugin) override fun register(value: Person) {
            TODO("Not yet implemented")
        }


    }

    object Person1 : Person(id("test:person1"), "Sandy", 25)
    object Person2 : Person(id("test:person2"), "Incompleteusern", 999)

}

internal class CodecTest {

    @Test
    fun make_codec() {
        Person.load()
        val codec = Person.codec()
        val expected = nbtOf(
            "type" to "test:person_type",
            "value" to listOf(
                nbtOf(
                    "id" to 0,
                    "name" to "test:person1",
                    "element" to nbtOf(
                        "name" to "Sandy",
                        "age" to 25
                    )
                ),
                nbtOf(
                    "id" to 1,
                    "name" to "test:person2",
                    "element" to nbtOf(
                        "name" to "Incompleteusern",
                        "age" to 999
                    )
                )
            )
        )
        assertEquals(expected, codec)
    }

}