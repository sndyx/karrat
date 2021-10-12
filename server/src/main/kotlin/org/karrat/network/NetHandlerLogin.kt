/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.utils.CryptManager
import org.karrat.Server
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.clientbound.EncryptionRequestPacket
import org.karrat.packet.login.clientbound.LoginSuccessPacket
import org.karrat.packet.login.serverbound.EncryptionResponsePacket
import org.karrat.packet.login.serverbound.LoginStartPacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.random.Random


open class NetHandlerLogin(val session: Session) : NetHandler {

    private var state: LoginState = LoginState.INITIAL
    private lateinit var username : String
    private val verificationToken: ByteArray = Random.nextBytes(4)
    
    override fun read(id: Int, data: ByteBuffer): ServerboundPacket = when (id) {
        0x00 -> LoginStartPacket(data)
        0x01 -> EncryptionResponsePacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket) = when (packet) {
        is LoginStartPacket -> {
            check(state == LoginState.INITIAL) { fatal("Unexpected Login Start Packet!") }
            username = packet.username
            state = LoginState.READY_FOR_ENCRYPTION
            session.send(EncryptionRequestPacket("", Server.keyPair.public.encoded, verificationToken))
        }
        is EncryptionResponsePacket -> {
            check(state == LoginState.READY_FOR_ENCRYPTION) { fatal("Unexpected Encryption Response Packet!") }
            check(verificationToken.contentEquals(packet.decodeVerificationToken(Server.keyPair.private))) { fatal("Invalid verification return!") }
            val sharedSecret: SecretKey = packet.decodeSharedSecret(Server.keyPair.private)
            
            val encryptCipher: Cipher = CryptManager.generateAESInstance(1, sharedSecret)
            val decryptCipher: Cipher = CryptManager.generateAESInstance(2, sharedSecret)
            session.enableEncryption(encryptCipher, decryptCipher)

            val hash = CryptManager.getServerIdHash("", Server.keyPair.public, sharedSecret).contentToString()
            
            state = LoginState.AUTHENTICATING;

            //TODO settings
            val prevent_proxy_connections: Boolean = false
            val socketaddress: SocketAddress = session.socket.remoteSocketAddress
            val ip: InetAddress? = if (prevent_proxy_connections && socketaddress is InetSocketAddress) socketaddress.address else null

            var url = if (ip != null) URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash&ip=$ip")
            else URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash")

            //TODO authenticate :)
            state = LoginState.READY_TO_ACCEPT
            
            session.enableCompression()
            session.send(LoginSuccessPacket(TODO(), username))
        }
        else -> fatal("Invalid packet to be handled.")
    }
    
    enum class LoginState {
        INITIAL,
        READY_FOR_ENCRYPTION,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        ACCEPTED;
    }
    
}