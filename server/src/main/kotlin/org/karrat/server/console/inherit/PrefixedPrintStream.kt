/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server.console.inherit

import java.io.OutputStream

public abstract class PrefixedPrintStream(out: OutputStream) : FormattedPrintStream(out) {

    protected abstract val prefix: String

    override fun format(text: String): String {
        return "$prefix | $text"
    }
    
}
