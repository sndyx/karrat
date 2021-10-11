/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import CryptManager
import org.karrat.packet.ServerboundPacket
import org.karrat.packet.login.clientbound.EncryptionRequestPacket
import org.karrat.packet.login.clientbound.LoginSuccessPacket
import org.karrat.packet.login.clientbound.SetCompressionPacket
import org.karrat.packet.login.serverbound.EncryptionResponsePacket
import org.karrat.packet.login.serverbound.LoginStartPacket
import org.karrat.server.fatal
import org.karrat.struct.ByteBuffer
import java.math.BigInteger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.SecretKey
import kotlin.random.Random


open class NetHandlerLogin(val session: Session) : INetHandler {

    private var state: LoginState = LoginState.Inital;

    private lateinit var username : String;

    private val verificationToken: ByteArray = Random.nextBytes(4)
    
    override fun read(id: Int, data: ByteBuffer): ServerboundPacket = when (id) {
        0x00 -> LoginStartPacket(data)
        0x01 -> EncryptionResponsePacket(data)
        else -> fatal("Invalid packet id $id in state handshake.")
    }

    override fun process(packet: ServerboundPacket) = when (packet) {
        is LoginStartPacket -> {
            check(state == LoginState.Inital) { "Unexpected Login Start Packet!" }
            username = packet.username;
            state = LoginState.READY_FOR_ENCYPTION;

            session.send(EncryptionRequestPacket("", session.keyPair.public.encoded, verificationToken))
        }
        is EncryptionResponsePacket -> {
            check(state == LoginState.READY_FOR_ENCYPTION) { "Unexpected Encryption Response Packet!" }
            check(verificationToken.contentEquals(packet.decodeVerificationToken(session.keyPair.private))) { "Invalid verification return!" }

            val sharedSecret : SecretKey = packet.decodeSharedSecret(session.keyPair.private)
            //TODO cipher

            val encrypter: Cipher = CryptManager.generateAESInstance(1, sharedSecret)
            val decrypter: Cipher = CryptManager.generateAESInstance(2, sharedSecret)

            val hash = BigInteger(CryptManager.getServerIdHash("", session.keyPair.public, sharedSecret)).toString(16)

            session.activateEncryption(encrypter, decrypter)

            state = LoginState.AUTHENTICATING;

            //TODO settings
            val prevent_proxy_connections: Boolean = false;
            val socketaddress: SocketAddress = session.socket.remoteSocketAddress
            val ip: InetAddress? = if (prevent_proxy_connections && socketaddress is InetSocketAddress) socketaddress.address else null

            lateinit var url: URL
            if (ip != null) {
                url = URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash&ip=$ip")
            } else {
                url = URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username=$username&serverId=$hash")
            }

            //TODO authenticate :)
            state = LoginState.READY_TO_ACCEPT

            val network_compression_threshold = 64;

            session.activateCompression(network_compression_threshold)
            session.send(LoginSuccessPacket(TODO(), username))
        }
        else -> fatal("Invalid packet to be handled.")
    }


    enum class LoginState {
        Inital,
        READY_FOR_ENCYPTION,
        AUTHENTICATING,
        READY_TO_ACCEPT,
        ACCEPTED;


    }
}