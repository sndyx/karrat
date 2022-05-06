/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server.console

import org.karrat.server.console.inherit.PrefixedPrintStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

public class SimplePrintStream(out: OutputStream) : PrefixedPrintStream(out) {
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
