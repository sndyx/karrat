/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.internal

import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

internal fun request(
    url: String,
    vararg parameters: Pair<String, String?> = emptyArray()
): Result<ByteArray> {
    val urlWithParameters = buildString {
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
    val connection = openHttpConnection(urlWithParameters)
    return runCatching {
        connection.inputStream
            .use { Result.success(it.readBytes()) }
    }.getOrElse {
        Result.failure(it)
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

internal fun hash(data: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(data)
}