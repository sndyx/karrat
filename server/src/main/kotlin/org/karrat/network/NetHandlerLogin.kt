/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.Config
import org.karrat.Server
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.clientbound.EncryptionRequestPacket
import org.karrat.packet.login.clientbound.LoginSuccessPacket
import org.karrat.packet.login.serverbound.EncryptionResponsePacket
import org.karrat.packet.login.serverbound.LoginStartPacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import java.net.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.random.Random

public open class NetHandlerLogin(public val session: Session) : NetHandler {

    private var state: LoginState = LoginState.INITIAL
    private lateinit var username : String
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
        is LoginStartPacket -> {
            check(state == LoginState.INITIAL) {
                fatal("Unexpected Login Start Packet!")
            }
            username = packet.username
            state = LoginState.READY_FOR_ENCRYPTION
            session.send(EncryptionRequestPacket("",
                Server.keyPair.public.encoded,
                verificationToken))
        }
        is EncryptionResponsePacket -> {
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
            
            state = LoginState.AUTHENTICATING

            val socketAddress: SocketAddress = session.socket.remoteSocketAddress
            val ip: InetAddress? =
                if (Config.preventProxyConnections
                    && socketAddress is InetSocketAddress)
                    socketAddress.address
                else null
            
            val url = if (ip != null) URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash&ip=$ip")
            else URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash")
            
            //TODO authenticate :)
            state = LoginState.READY_TO_ACCEPT
            
            session.enableCompression()
            session.send(LoginSuccessPacket(TODO(), username))
        }
        else -> fatal("Invalid packet to be handled.")
    }
    
    public enum class LoginState {
        INITIAL,
        READY_FOR_ENCRYPTION,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        ACCEPTED;
    }
    
}