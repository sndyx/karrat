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
        is NetHandlerHandshake -> SessionState.Handshake
        is NetHandlerStatus -> SessionState.Status
        is NetHandlerPlay -> SessionState.Play
        is NetHandlerLogin -> SessionState.Login
        else -> SessionState.Handshake
    }