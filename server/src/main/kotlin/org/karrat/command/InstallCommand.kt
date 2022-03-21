/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import org.karrat.internal.request
import org.karrat.play.colored
import org.karrat.response.PackageResponse

internal fun Command.Companion.installCommand(): Command =
    command("install") {
        argument<String>().onRun {
            val pkg: String = args[0]
            request<PackageResponse>("https://api.karrat.org/package/$pkg")
                .onSuccess {
                    respond("&aStuff lol!! 1billion megabuytes? Continue!? [yes] [no]")
                }.onFailure {
                    respond("&cUnable to locate package: '$pkg.'".colored())
                }
        }
    }