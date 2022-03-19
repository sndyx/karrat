/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network

import org.karrat.network.handler.NetHandlerHandshake
import org.karrat.network.handler.NetHandlerLogin
import org.karrat.network.handler.NetHandlerPlay
import org.karrat.network.handler.NetHandlerStatus

public val Session.state: SessionState
    get() = when (netHandler) {
        is NetHandlerHandshake -> SessionState.HANDSHAKE
        is NetHandlerStatus -> SessionState.STATUS
        is NetHandlerPlay -> SessionState.PLAY
        is NetHandlerLogin -> SessionState.LOGIN
        else -> SessionState.HANDSHAKE
    }