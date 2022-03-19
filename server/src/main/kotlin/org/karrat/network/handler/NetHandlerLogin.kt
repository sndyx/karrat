/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network.handler

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.BannedPlayerLoginEvent
import org.karrat.event.PlayerLoginEvent
import org.karrat.event.dispatchEvent
import org.karrat.network.NetHandler
import org.karrat.network.Session
import org.karrat.network.authenticate
import org.karrat.network.translation.decodeSharedSecret
import org.karrat.network.translation.decodeVerificationToken
import org.karrat.network.translation.generateAESInstance
import org.karrat.network.translation.getServerIdHash
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.EncryptionRequestPacket
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.packet.login.LoginStartPacket
import org.karrat.packet.login.LoginSuccessPacket
import org.karrat.struct.ByteBuffer
import org.karrat.struct.Uuid
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.concurrent.thread
import kotlin.random.Random

public open class NetHandlerLogin(private val session: Session) : NetHandler {

    public var state: LoginState = LoginState.Initial
    public lateinit var username: String
    public lateinit var uuid: Uuid
    public val verificationToken: ByteArray by lazy { Random.nextBytes(4) }

    override fun read(
        id: Int,
        data: ByteBuffer
    ): ServerboundPacket = when (id) {
        0x00 -> LoginStartPacket(data)
        0x01 -> EncryptionResponsePacket(data)
        else -> error("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is LoginStartPacket -> handleLoginStartPacket(packet)
        is EncryptionResponsePacket -> handleEncryptionResponsePacket(packet)
        else -> error("Invalid packet to be handled.")
    }

    internal fun handleLoginStartPacket(packet: LoginStartPacket) {
        check(state == LoginState.Initial) {
            error("Unexpected Login Start Packet!")
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
            error("Unexpected Encryption Response Packet!")
        }
        check(verificationToken contentEquals packet.decodeVerificationToken(Server.keyPair.private)) {
            error("Invalid verification return!")
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

        thread(name = "auth") {
    
            val result = Server.authenticate(hash, session.address, username)
    
            result.onSuccess { response ->

                state = LoginState.ReadyToAccept
        
                if (uuid in Config.bannedPlayers) {
                    val event = BannedPlayerLoginEvent(uuid, username)
                    if (Server.dispatchEvent(event)) {
                        session.disconnect(event.message)
                        return@thread
                    }
                }
        
                session.player = Player(session, uuid, username, location = Config.spawnLocation)

                response.properties.firstOrNull { it.name == "textures" }
                    ?.let { session.player.skin = it.value }
        
                if (Server.dispatchEvent(PlayerLoginEvent(session.player))) {
                    session.disconnect("Unable to join server.")
                    return@thread
                }
        
                session.enableCompression()
                session.netHandler = NetHandlerPlay(session)
                session.send(LoginSuccessPacket(uuid, username))
            }
    
            result.onFailure {
                session.disconnect("Failed to authenticate user.")
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