/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.Command
import org.karrat.command.argument
import org.karrat.command.command
import org.karrat.command.route
import org.karrat.play.BlockPos

public fun Command.Companion.complexCommand(): Command =
    command("complex-command") {
        route("coords") {
            argument<BlockPos>().onRun {
                val pos: BlockPos = arg(0)
                respond("Your coords were: [x: ${pos.xPos}, y: ${pos.yPos}, z: ${pos.zPos}]")
            }
        }
    }