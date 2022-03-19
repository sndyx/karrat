/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.karrat.Config
import org.karrat.Server
import org.karrat.internal.request
import org.karrat.network.entity.SessionServerResponse
import java.net.InetAddress

public open class AuthServer {
    
    @OptIn(ExperimentalSerializationApi::class)
    public open fun authenticate(serverHash: ByteArray, ip: InetAddress, username: String): Result<SessionServerResponse> {
        val result = request(
            "${Config.sessionServer}/session/minecraft/hasJoined",
            "username" to username,
            "serverId" to serverHash.toString(),
            "ip" to ip.hostAddress
        )
        if (result.isFailure) return Result.failure(result.exceptionOrNull()!!)
        val response = Json.decodeFromString<SessionServerResponse>(
            result.getOrThrow().decodeToString()
        )
        return Result.success(response)
    }
    
}

public fun Server.authenticate(hash: ByteArray, ip: InetAddress, username: String): Result<SessionServerResponse> {
    return auth.authenticate(hash, ip, username)
}