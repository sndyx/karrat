/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class UuidTest {

    @Test
    fun stringify() {
        val hexCodes = "1234567890abcdef"

        for (i in 1..10000) {
            val builder: StringBuilder = StringBuilder()

            //Generate random hex
            for (j in 1..32) {
                builder.append(hexCodes[Random.nextInt(15)])
            }

            val uuid = Uuid(builder.toString())

            println(builder.toString())
            println(uuid)

            check(builder.toString() == uuid.toString().replace("-", "")) {
                "Uuid doesn't stringify correctly: $builder and ${Uuid(builder.toString())}"
            }
        }
    }
}