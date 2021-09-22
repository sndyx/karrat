/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.test

import org.karrat.command.ICommand
import org.karrat.entity.Player
import org.karrat.plugin.Command

@Command("ExampleCommand", "Example")
object MyCommand : ICommand {
    
    override fun execute(sender: Player) {
    
    }
    
}