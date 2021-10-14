/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, vararg properties: Pair<String, String>): Result<ByteArray> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    properties.forEach {
        connection.addRequestProperty(it.first, it.second)
    }
    
    return try {
        Result.success(connection.inputStream.readBytes())
    } catch (e : IOException) {
        Result.failure(Exception())
    }
}

internal fun request(url: String): Result<ByteArray> {
    val connection = URL(url).openConnection() as HttpURLConnection
    return try {
        Result.success(connection.inputStream.readBytes())
    } catch (e : IOException) {
        Result.failure(Exception())
    }
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}

internal typealias NioByteBuffer = java.nio.ByteBuffer
