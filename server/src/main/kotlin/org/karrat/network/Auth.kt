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
import org.karrat.response.SessionServerResponse
import java.net.InetAddress

public open class AuthServer {
    
    @OptIn(ExperimentalSerializationApi::class)
    public open fun authenticate(serverHash: ByteArray, ip: InetAddress, username: String): Result<SessionServerResponse> {
        val response = request<SessionServerResponse>(
            "${Config.sessionServer}/session/minecraft/hasJoined",
            "username" to username,
            "serverId" to serverHash.toString(),
            "ip" to ip.hostAddress
        )
        if (response.isFailure) return Result.failure(response.exceptionOrNull()!!)
        return Result.success(response.getOrThrow())
    }
    
}

public fun Server.authenticate(hash: ByteArray, ip: InetAddress, username: String): Result<SessionServerResponse> {
    return auth.authenticate(hash, ip, username)
}