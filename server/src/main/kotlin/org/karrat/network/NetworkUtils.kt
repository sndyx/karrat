/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network

val Session.state
get() = when (netHandler) {
    is NetHandlerHandshake -> SessionState.HANDSHAKE
    else -> SessionState.HANDSHAKE
}