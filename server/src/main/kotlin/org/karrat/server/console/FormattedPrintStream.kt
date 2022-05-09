/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server.console

import java.io.OutputStream
import java.io.PrintStream

internal abstract class FormattedPrintStream(out: OutputStream) : PrintStream(out) {

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

    public abstract fun format(text: String): String
}
