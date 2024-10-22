/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.server.console

import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

internal class SimplePrintStream(out: OutputStream) : PrefixedPrintStream(out) {
    private val time: String
        get() {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return formatter.format(time)
        }

    override val prefix: String
        get() {
            val format = "$time <@${Thread.currentThread().name}>"
            return if (format.length > 40) format.substring(0, 36) + "...>"
            else format.padEnd(40)
        }
}
