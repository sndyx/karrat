/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, vararg properties: Pair<String, String>): Result<ByteArray> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    properties.forEach {
        connection.addRequestProperty(it.first, it.second)
    }
    
    return runCatching {
        connection.inputStream
            .use { Result.success(it.readBytes()) }
    }.getOrElse {
        Result.failure(Exception())
    }
}

internal fun request(url: String): Result<ByteArray> {
    val connection = URL(url).openConnection() as HttpURLConnection
    
    return runCatching {
        connection.inputStream
            .use { Result.success(it.readBytes()) }
    }.getOrElse {
        Result.failure(Exception())
    }
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}