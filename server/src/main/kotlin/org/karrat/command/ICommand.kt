/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.command

import org.karrat.entity.Player

interface ICommand {
    
    fun execute(sender: Player)
    
}