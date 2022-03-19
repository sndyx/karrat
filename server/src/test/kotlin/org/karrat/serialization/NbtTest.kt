/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.serialization

import kotlinx.serialization.Serializable
import org.karrat.serialization.nbt.Nbt
import org.karrat.struct.nbtOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NbtTest {

    private val testNbt =
        nbtOf(
            "height" to 64.3f,
            "color" to 0xff0000,
            "stats" to nbtOf(
                "damage" to 10.5f,
                "health" to 20.0f,
                "attributes" to listOf(
                    "glowing",
                    "invisible"
                )
            ),
            "name" to "Barry"
        )
    
    private val testCreature =
        Creature(
            height = 64.3f,
            color = 0xff0000,
            stats = CreatureStats(
                damage = 10.5f,
                health = 20.0f,
                attributes = listOf(
                    "glowing",
                    "invisible"
                )
            ),
            name = "Barry"
        )
    
    @Serializable
    internal data class Creature(var height: Float, var color: Int, var name: String, var stats: CreatureStats)
    
    @Serializable
    internal data class CreatureStats(var damage: Float, var health: Float, var attributes: List<String>)
    
    @Test
    fun testNbt() {
        val testCreatureNbt = Nbt.encodeToNbt(testCreature)
        assertEquals(testNbt, testCreatureNbt)
        
        val bytes = Nbt.encodeToBytes(testCreatureNbt)
        val reconstructed = Nbt.decodeFromBytes(bytes)
        val creature = Nbt.decodeFromNbt<Creature>(reconstructed)
        assertEquals(testCreature, creature)
    }

}