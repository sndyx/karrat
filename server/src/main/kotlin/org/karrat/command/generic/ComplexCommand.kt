/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command.generic

import org.karrat.command.*
import org.karrat.struct.BlockPos

public fun Command.CommandRegistry.complexCommand(): Command =
    command("complex-command") {
        route("coords") {
            argument<BlockPos>().onRun {
                val pos: BlockPos = args.get(0)
                respond("Your coords were: [x: ${pos.x}, y: ${pos.y}, z: ${pos.z}]")
            }
        }
    }