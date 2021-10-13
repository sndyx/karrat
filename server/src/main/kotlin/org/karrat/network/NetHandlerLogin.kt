/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.internal.request
import org.karrat.network.entity.SessionServerResponse
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.clientbound.EncryptionRequestPacket
import org.karrat.packet.login.clientbound.LoginSuccessPacket
import org.karrat.packet.login.serverbound.EncryptionResponsePacket
import org.karrat.packet.login.serverbound.LoginStartPacket
import org.karrat.server.fatal
import org.karrat.server.info
import org.karrat.struct.ByteBuffer
import org.karrat.struct.Uuid
import java.io.IOException
import java.net.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.concurrent.thread
import kotlin.random.Random


public open class NetHandlerLogin(public val session: Session) : NetHandler {

    private var state: LoginState = LoginState.INITIAL
    private lateinit var username : String
    private lateinit var uuid: Uuid
    private val verificationToken: ByteArray = Random.nextBytes(4)
    
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
        check(state == LoginState.INITIAL) {
            fatal("Unexpected Login Start Packet!")
        }
        username = packet.username
        state = LoginState.READY_FOR_ENCRYPTION
        session.send(EncryptionRequestPacket("",
            Server.keyPair.public.encoded,
            verificationToken))
    }
    
    internal fun handleEncryptionResponsePacket(packet: EncryptionResponsePacket) {
        check(state == LoginState.READY_FOR_ENCRYPTION) {
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
    
        val hash = getServerIdHash("",
            Server.keyPair.public, sharedSecret).contentToString()
    
        authenticate(hash)
    }
    
    internal fun authenticate(hash: String) {
        state = LoginState.AUTHENTICATING
    
        val socketAddress: SocketAddress = session.socket.remoteAddress
        val ip: InetAddress? =
            if (Config.preventProxyConnections
                && socketAddress is InetSocketAddress)
                socketAddress.address
            else null
    
        thread(name="auth") {
            try {
                val content = request("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash${ip?.let { "&ip=$ip" }}")

                if (!content.error) {
                    val response = Json.decodeFromString<SessionServerResponse>(content.result)
                    //TODO add properties to player
                    uuid = response.uuid
                    state = LoginState.READY_TO_ACCEPT

                    session.player = Player(uuid, Config.spawnLocation)
                    response.properties.firstOrNull { it.name == "textures" }
                        ?.let { session.player.skin = it.value }

                    session.enableCompression()
                    session.send(LoginSuccessPacket(uuid, username))
                } else {
                    session.disconnect("Failed to verify username!")
                    info("Username '$username' tried to join with an invalid session")
                }
            } catch (e: Exception) {
                //TODO make sure it stops throwing exceptions
                e.printStackTrace()
            }

            session.disconnect("Authentication servers are down. Please try again later, sorry!")
        }
    }
    
    public enum class LoginState {
        INITIAL,
        READY_FOR_ENCRYPTION,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        ACCEPTED;
    }
    
}