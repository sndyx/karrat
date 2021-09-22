/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.util

typealias NetSocket = java.net.ServerSocket

fun Boolean.then(block: () -> Unit) {
    if (this) block.invoke()
}