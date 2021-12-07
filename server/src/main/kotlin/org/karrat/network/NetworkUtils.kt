/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

public val Session.state: SessionState
get() = when (netHandler) {
    is NetHandlerHandshake -> SessionState.HANDSHAKE
    is NetHandlerStatus -> SessionState.STATUS
    is NetHandlerPlay -> SessionState.PLAY
    is NetHandlerLogin -> SessionState.LOGIN
    else -> SessionState.HANDSHAKE
}