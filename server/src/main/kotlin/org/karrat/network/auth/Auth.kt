/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.auth

import org.karrat.Config
import org.karrat.Server
import org.karrat.internal.*
import org.karrat.internal.request
import org.karrat.packet.login.LoginStartPacket
import org.karrat.response.SessionServerResponse
import java.math.BigInteger
import java.net.InetAddress
import java.security.PublicKey
import javax.crypto.SecretKey

public open class AuthServer {
    
    public open fun authenticate(secretKey: SecretKey, ip: InetAddress, username: String): Result<SessionServerResponse> {
        val serverHash = BigInteger(
            digestOperation(
                "".toByteArray(Charsets.ISO_8859_1), secretKey.encoded, Server.keyPair.public.encoded
            )
        ).toByteArray()

        val response = request<SessionServerResponse>(
            "${Config.sessionServer}/session/minecraft/hasJoined",
            "username" to username,
            "serverId" to BigInteger(serverHash).toString(16)
        )
        if (response.isFailure) return Result.failure(response.exceptionOrNull()!!)
        return Result.success(response.getOrThrow())
    }
}

public val mojangPublicKey: PublicKey by lazy {
    // TODO fail more gracefully
    byteArrayToRSA(resourceAsBytes("/yggdrasil_session_pubkey.der")!!)
}

public val LoginStartPacket.publicKey: PublicKey
    get() = byteArrayToRSA(publicKeyArray)
