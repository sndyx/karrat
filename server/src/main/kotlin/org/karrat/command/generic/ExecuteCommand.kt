/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command.generic

import org.karrat.command.*

internal fun Command.CommandRegistry.executeCommand(): Command =
    command("execute") execute@{
        
        route("align") {
            argument<String>("align", "axes") {
                redirect(this@execute)
            }
        }
        
        route("anchored") {
            argument<String>("anchored", "anchor") {
                redirect(this@execute)
            }
        }
        
        route("as") {
            argument<String>("as", "targets") {
                redirect(this@execute)
            }
        }
        
        route("at") {
            argument<String>("at","targets") {
                redirect(this@execute)
            }
        }
        
        route("facing") {
            argument<String>("facing", "pos") {
                redirect(this@execute)
            }
            route("entity") {
                argument<String>("entity/targets", "targets") {
                    argument<String>("entity/anchor", "anchor") {
                        redirect(this@execute)
                    }
                }
            }
        }
        
    }