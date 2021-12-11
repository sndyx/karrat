/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network.handler

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.dispatchEvent
import org.karrat.event.BannedPlayerLoginEvent
import org.karrat.event.PlayerLoginEvent
import org.karrat.internal.request
import org.karrat.network.NetHandler
import org.karrat.network.Session
import org.karrat.network.entity.SessionServerResponse
import org.karrat.network.translation.decodeSharedSecret
import org.karrat.network.translation.decodeVerificationToken
import org.karrat.network.translation.generateAESInstance
import org.karrat.network.translation.getServerIdHash
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.EncryptionRequestPacket
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.packet.login.LoginStartPacket
import org.karrat.packet.login.LoginSuccessPacket
import org.karrat.server.fatal
import org.karrat.server.info
import org.karrat.struct.ByteBuffer
import org.karrat.struct.Uuid
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.concurrent.thread
import kotlin.random.Random

public open class NetHandlerLogin(public val session: Session) : NetHandler {

    private var state: LoginState = LoginState.Initial
    private lateinit var username: String
    private lateinit var uuid: Uuid
    private val verificationToken: ByteArray by lazy { Random.nextBytes(4) }

    override fun read(
        id: Int,
        data: ByteBuffer
    ): ServerboundPacket = when (id) {
        0x00 -> LoginStartPacket(data)
        0x01 -> EncryptionResponsePacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is LoginStartPacket -> handleLoginStartPacket(packet)
        is EncryptionResponsePacket -> handleEncryptionResponsePacket(packet)
        else -> fatal("Invalid packet to be handled.")
    }

    internal fun handleLoginStartPacket(packet: LoginStartPacket) {
        check(state == LoginState.Initial) {
            fatal("Unexpected Login Start Packet!")
        }
        username = packet.username
        state = LoginState.ReadyForEncryption
        session.send(
            EncryptionRequestPacket(
                "",
                Server.keyPair.public.encoded,
                verificationToken
            )
        )
    }

    internal fun handleEncryptionResponsePacket(packet: EncryptionResponsePacket) {
        check(state == LoginState.ReadyForEncryption) {
            fatal("Unexpected Encryption Response Packet!")
        }
        check(verificationToken contentEquals packet.decodeVerificationToken(Server.keyPair.private)) {
            fatal("Invalid verification return!")
        }
        val sharedSecret: SecretKey =
            packet.decodeSharedSecret(Server.keyPair.private)

        val encryptCipher: Cipher =
            generateAESInstance(1, sharedSecret)
        val decryptCipher: Cipher =
            generateAESInstance(2, sharedSecret)
        session.enableEncryption(encryptCipher, decryptCipher)

        val hash = getServerIdHash(
            "",
            Server.keyPair.public, sharedSecret
        )

        authenticate(hash)
    }

    internal fun authenticate(hash: String) {
        state = LoginState.Authenticating

        val socketAddress: SocketAddress = session.socket.remoteAddress
        val ip: InetAddress? =
            if (Config.preventProxyConnections
                && socketAddress is InetSocketAddress
            )
                socketAddress.address
            else null

        thread(name = "auth") {
            val content =
                request(
                    "${Config.sessionServer}/session/minecraft/hasJoined",
                    "username" to username,
                    "serverId" to hash,
                    "ip" to ip?.hostAddress
                )
            if (content.isSuccess) {
                val response = Json.decodeFromString<SessionServerResponse>(
                    content.getOrThrow().decodeToString()
                )
                // TODO add properties to player
                uuid = response.uuid
                state = LoginState.ReadyToAccept

                //TODO make a json so server can auto write to it and /ban can be implemented
                if (uuid in Config.bannedPlayers) {
                    val event = BannedPlayerLoginEvent(uuid, username)
                    if (Server.dispatchEvent(event)) {
                        session.disconnect(event.message)
                        return@thread
                    }
                }

                session.player = Player(uuid, username, location = Config.spawnLocation)
                response.properties.firstOrNull { it.name == "textures" }
                    ?.let { session.player!!.skin = it.value }

                if (Server.dispatchEvent(PlayerLoginEvent(session.player!!))) {
                    session.disconnect("Unable to join server.")
                    return@thread
                }

                session.enableCompression()
                session.netHandler = NetHandlerPlay()
                session.send(LoginSuccessPacket(uuid, username))
            } else {
                session.disconnect("Failed to verify username!")
                info("Username '$username' tried to join with an invalid session")
            }
        }
    }

    public enum class LoginState {
        Initial,
        ReadyForEncryption,
        Authenticating,
        ReadyToAccept,
        Accepted;
    }

}