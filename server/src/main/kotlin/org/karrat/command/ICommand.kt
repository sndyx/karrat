/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.entity.Player

public interface ICommand {

    public fun execute(sender: Player)

}