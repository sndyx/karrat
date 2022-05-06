/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.net.*
import java.security.MessageDigest
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.system.exitProcess

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> request(
    url: String,
    vararg parameters: Pair<String, String?> = emptyArray()
): Result<T> {
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
            .use { Result.success(Json.decodeFromStream<T>(it)) }
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

internal fun exitProcessWithMessage(message: String, status: Int) {
    println(message)
    Thread.sleep(1000L)
    exitProcess(status)
}

internal fun <T> lazyMutable(initializer: () -> T): LazyMutable<T> =
    LazyMutable(initializer)

internal class LazyMutable<T>(val initializer: () -> T) : ReadWriteProperty<Any?, T> {

    private object Null
    private var prop: Any? = Null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (prop == Null) {
            synchronized(this) {
                return if (prop == Null) initializer().also { prop = it } else prop as T
            }
        } else prop as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        synchronized(this) {
            prop = value
        }
    }

}

internal fun Result.Companion.success(): Result<Unit> = success(Unit)

@PublishedApi
internal fun unreachable(): Nothing = throw IllegalStateException("Unreachable should never be called.")