/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, requestProperties: Map<String, String>): RequestResult {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    for (entry: Map.Entry<String, String> in requestProperties.entries) {
        connection.addRequestProperty(entry.key, entry.value)
    }

    try {
        return RequestResult(false, connection.inputStream.readBytes().decodeToString())
    } catch (e : IOException) {
        return RequestResult(false, connection.errorStream.readBytes().decodeToString())
    }
}

internal fun request(url: String): RequestResult {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000

    try {
        return RequestResult(false, connection.inputStream.readBytes().decodeToString())
    } catch (e : IOException) {
        return RequestResult(false, connection.errorStream.readBytes().decodeToString())
    }
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}

internal data class RequestResult(val error: Boolean, val result: String)
