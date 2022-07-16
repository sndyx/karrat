/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.internal.request
import org.karrat.response.SessionServerResponse
import java.math.BigInteger
import java.net.InetAddress

public open class AuthServer {
    
    public open fun authenticate(serverHash: ByteArray, ip: InetAddress, username: String): Result<SessionServerResponse> {
        val response = request<SessionServerResponse>(
            "${Config.sessionServer}/session/minecraft/hasJoined",
            "username" to username,
            "serverId" to BigInteger(serverHash).toString(16)
        )
        if (response.isFailure) return Result.failure(response.exceptionOrNull()!!)
        return Result.success(response.getOrThrow())
    }
    
}