/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.internal

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal fun request(url: String, vararg properties: Pair<String, String>): RequestResult {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    properties.forEach {
        connection.addRequestProperty(it.first, it.second)
    }

    var result : String
    var activeInputStream: InputStream? = null

    try {
        activeInputStream = connection.inputStream
        result = activeInputStream.readBytes().decodeToString()
        activeInputStream.close()
        return RequestResult(ResponseState.SUCCESS, result)
    } catch (e : IOException) {
        e.printStackTrace()
    }

    //Failed inputStream, trying errorStream
    activeInputStream?.close()

    try {
        activeInputStream = connection.errorStream
        result = activeInputStream.readBytes().decodeToString()
        activeInputStream.close()
        return RequestResult(ResponseState.ERROR, result)
    } catch (e : IOException) {
        e.printStackTrace()
    }

    activeInputStream?.close()

    //Yo everything failed
    return RequestResult(ResponseState.FAILED, "")
}

internal fun postRequest(url: String, requestProperties: Map<String, String>): String {
    TODO() //when a post request is needed >:)
}

internal data class RequestResult(val resultState: ResponseState, val result: String)

internal enum class ResponseState {
    SUCCESS,
    ERROR,
    FAILED
}

internal typealias NioByteBuffer = java.nio.ByteBuffer
