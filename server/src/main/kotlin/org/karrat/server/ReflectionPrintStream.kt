/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.server

import java.io.OutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.*

public class ReflectionPrintStream(out: OutputStream) : PrintStream(out) {
    
    private val time: String
        get() {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return formatter.format(time)
        }
    
    private val prefix: String
        get() {
            val caller = Thread.currentThread().stackTrace[4].fileName
            val format = "$time <$caller${
                if (Thread.currentThread().name == "main") ""
                else " @${Thread.currentThread().name}"
            }>"
            return if (format.length > 40) format.substring(0, 36) + "...>"
            else format.padEnd(40)
        }
    
    override fun println(x: Any?) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: Boolean) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: Char) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: CharArray) {
        super.println(format(x.contentToString()))
    }
    
    override fun println(x: Double) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: Float) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: Int) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: Long) {
        super.println(format(x.toString()))
    }
    
    override fun println(x: String?) {
        super.println(format(x.toString()))
    }
    
    private fun format(text: String): String {
        return "$prefix | $text"
    }
    
}