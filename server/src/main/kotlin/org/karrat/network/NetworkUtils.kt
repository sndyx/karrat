/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

val Session.state
get() = when (packetAdapter) {
    is PacketAdapterHandshake -> SessionState.HANDSHAKE
    is PacketAdapterPlay -> SessionState.PLAY
    else -> SessionState.HANDSHAKE
}