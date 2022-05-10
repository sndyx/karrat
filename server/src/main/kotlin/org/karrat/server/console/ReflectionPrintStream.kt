/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server.console

import org.karrat.Config
import org.karrat.Server
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

internal class ReflectionPrintStream(out: OutputStream) : PrefixedPrintStream(out) {

    private val time: String
        get() {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return formatter.format(time)
        }

    override val prefix: String
        get() {
            val caller = Thread.currentThread().stackTrace[4]
            val pkg = caller.className.substringBeforeLast('.')
            val pluginName = Server.plugins
                .find { pkg.startsWith(it.pluginClass.packageName) }?.name
            val location = pluginName ?: caller.fileName
            val format = "$time <$location${
                if (Thread.currentThread().id == Config.mainThreadId
                    || Thread.currentThread().name.startsWith("worker-thread")
                ) ""
                else " @${Thread.currentThread().name}"
            }>"
            return if (format.length > 40) format.substring(0, 36) + "...>"
            else format.padEnd(40)
        }
    
}