/*
 * Copyright © Karrat - 2021.
 */

package org.karrat

import org.karrat.network.ServerSocket

object Server {
    
    fun start(port: Int) {
        ServerSocket.start(port)
    }

}