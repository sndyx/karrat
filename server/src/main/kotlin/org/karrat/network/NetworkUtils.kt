/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

import org.karrat.network.handlers.NetHandlerHandshake
import org.karrat.network.handlers.NetHandlerLogin
import org.karrat.network.handlers.NetHandlerPlay
import org.karrat.network.handlers.NetHandlerStatus

public val Session.state: SessionState
get() = when (netHandler) {
    is NetHandlerHandshake -> SessionState.HANDSHAKE
    is NetHandlerStatus -> SessionState.STATUS
    is NetHandlerPlay -> SessionState.PLAY
    is NetHandlerLogin -> SessionState.LOGIN
    else -> SessionState.HANDSHAKE
}