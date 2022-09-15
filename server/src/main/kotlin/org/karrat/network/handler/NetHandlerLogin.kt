/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.handler

import org.karrat.Config
import org.karrat.Server
import org.karrat.entity.Player
import org.karrat.event.BannedPlayerLoginEvent
import org.karrat.event.PlayerLoginEvent
import org.karrat.event.dispatchEvent
import org.karrat.network.Session
import org.karrat.network.auth.MessageKeyInfo
import org.karrat.network.translation.*
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.EncryptionRequestPacket
import org.karrat.packet.login.EncryptionResponsePacket
import org.karrat.packet.login.LoginStartPacket
import org.karrat.packet.login.LoginSuccessPacket
import org.karrat.struct.*
import java.security.Signature
import java.time.Instant
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.concurrent.thread
import kotlin.random.Random


public open class NetHandlerLogin(private val session: Session) : NetHandler {

    public var state: LoginState = LoginState.Initial
    public lateinit var username: String
    public lateinit var uuid: Uuid
    public val verificationToken: ByteArray by lazy { Random.nextBytes(4) }

    public lateinit var messageData: MessageKeyInfo

    override fun read(
        id: Int,
        data: ByteBuffer
    ): ServerboundPacket = when (id) {
        0x00 -> LoginStartPacket(data)
        0x01 -> EncryptionResponsePacket(data)
        else -> error("Invalid packet id $id in state play.")
    }

    override fun process(packet: ServerboundPacket): Unit = when (packet) {
        is LoginStartPacket -> handleLoginStartPacket(packet)
        is EncryptionResponsePacket -> handleEncryptionResponsePacket(packet)
        else -> error("Invalid packet to be handled.")
    }

    internal fun handleLoginStartPacket(packet: LoginStartPacket) {
        check(state == LoginState.Initial) { "Unexpected Login Start Packet!" }
        username = packet.username
        state = LoginState.ReadyForEncryption

        if (Config.chatReports) {
            if (!packet.hasSigData) {
                session.disconnect("No public key.")
            }

            messageData = MessageKeyInfo(packet.publicKey, packet.signatureArray, packet.timestamp)
        }

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
            "Unexpected Encryption Response Packet!" }
        checkDecryption(Server.keyPair.private, packet.verifyToken, verificationToken) {
            "Invalid verification return!" }

        val sharedSecret: SecretKey =
            packet.getSharedSecret(Server.keyPair.private)

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
            val result = Server.auth.authenticate(hash, session.address, username)
            result.onSuccess { response ->
                uuid = response.uuid
                state = LoginState.ReadyToAccept

                if (uuid in Config.bannedPlayers) {
                    val event = BannedPlayerLoginEvent(uuid, username)
                    if (Server.dispatchEvent(event)) {
                        session.disconnect(event.message)
                        return@thread
                    }
                }

                if (Config.chatReports) {
                    // TODO check format is correct
                    if (messageData.expiresAt > Instant.now().toEpochMilli()) {
                        session.disconnect("Outdated public key")
                    }

                    val encodedKey: ByteArray = messageData.key.encoded

                    // TODO cache or something
                    val bytes: ByteArray =
                        Thread.currentThread().contextClassLoader.getResourceAsStream("/yggdrasil_session_pubkey.der").readAllBytes()
                    val mojangPublicKey = byteArrayToRSA(bytes)

                    // Input initial data
                    val toDigest = MutableByteBuffer(24 + encodedKey.size)
                    toDigest.writeUuid(uuid)
                    toDigest.writeLong(messageData.expiresAt)
                    toDigest.writeBytes(encodedKey)

                    val signature: Signature = Signature.getInstance("SHA1withRSA")
                    signature.initVerify(mojangPublicKey)
                    signature.update(toDigest.bytes)
                    if (!signature.verify(messageData.signature)) {
                        session.disconnect("Invalid public key")
                    }
                }

                session.player = Player(session, uuid, username, messageData, location = Config.spawnLocation)

                response.properties.firstOrNull { it.name == "textures" }
                    ?.let { session.player.skin = it.value }
        
                if (Server.dispatchEvent(PlayerLoginEvent(session.player))) {
                    session.disconnect("Unable to join server.")
                    return@thread
                }
                // session.enableCompression()
                session.netHandler = NetHandlerPlay(session)
                session.send(LoginSuccessPacket(uuid, username))


            }
            result.onFailure {
                it.printStackTrace()
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