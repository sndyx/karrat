/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.entity.Player

public class EvaluatedCommand(public val command: Command, public val args: CommandArguments) {
    
    public fun run(sender: Player?) {
        val scope = CommandScope(sender, args)
        command.executor.execute(scope)
    }
    
}