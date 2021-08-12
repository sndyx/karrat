/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.test

import org.karrat.Client
import org.karrat.auth.AuthenticationException

suspend fun main() {
    try {
        val client = Client("underscoreoptical@gmail.com", "00101010Aa%")
        println(client.token)
    } catch (exception: AuthenticationException) {
        exception.printStackTrace()
    }
}