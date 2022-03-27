/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.*

internal fun Command.CommandRegistry.executeCommand(): Command =
    command("execute") execute@{
        
        route("align") {
            argument<String>("axes") {
                redirect(this@execute)
            }
        }
        
        route("anchored") {
            argument<String>("anchor") {
                redirect(this@execute)
            }
        }
        
        route("as") {
            argument<String>("targets") {
                redirect(this@execute)
            }
        }
        
        route("at") {
            argument<String>("targets") {
                redirect(this@execute)
            }
        }
        
        route("facing") {
            argument<String>("pos") {
                redirect(this@execute)
            }
            route("entity") {
                argument<String>("targets") {
                    argument<String>("anchor") {
                        redirect(this@execute)
                    }
                }
            }
        }
        
    }