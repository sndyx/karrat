/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, vararg properties: Pair<String, String>): RequestResult {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    properties.forEach {
        connection.addRequestProperty(it.first, it.second)
    }
    
    return try {
        RequestResult(false, connection.inputStream.readBytes().decodeToString())
    } catch (e : IOException) {
        RequestResult(true, connection.errorStream.readBytes().decodeToString())
    }
}

internal fun request(url: String): RequestResult {
    val connection = URL(url).openConnection() as HttpURLConnection
    return try {
        RequestResult(false, connection.inputStream.readBytes().decodeToString())
    } catch (exception: IOException) {
        RequestResult(true, connection.errorStream.readBytes().decodeToString())
    }
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}

internal data class RequestResult(val error: Boolean, val result: String)
