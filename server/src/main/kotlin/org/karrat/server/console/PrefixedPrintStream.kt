/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.server.console

import java.io.OutputStream

internal abstract class PrefixedPrintStream(out: OutputStream) : FormattedPrintStream(out) {

    protected abstract val prefix: String

    override fun format(text: String): String {
        return "$prefix | $text"
    }
    
}
