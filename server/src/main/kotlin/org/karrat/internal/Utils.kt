/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import org.karrat.struct.ByteBuffer
import org.karrat.struct.array
import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, vararg parameters: Pair<String, String?>): Result<ByteArray> {
    buildString {
        append(url)
        append('?')
        parameters
            .filterNot { it.second == null }
            .forEach {
                append(it.first)
                append('=')
                append(it.second)
                append('&')
            }
        setLength(length - 1)
    }
    val connection = openHttpConnection(url)
    return runCatching {
        connection.inputStream
            .use { Result.success(it.readBytes()) }
    }.getOrElse {
        Result.failure(Exception())
    }
}

internal fun request(url: String): Result<ByteArray> {
    val connection = openHttpConnection(url)
    return runCatching {
        connection.inputStream
            .use { Result.success(it.readBytes()) }
    }.getOrElse {
        Result.failure(Exception())
    }
}

private fun openHttpConnection(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    return connection
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}