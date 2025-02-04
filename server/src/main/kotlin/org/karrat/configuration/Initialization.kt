/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.configuration

import org.karrat.Server
import org.karrat.internal.exitProcessWithMessage
import org.karrat.internal.resourceAsBytes
import java.util.*
import kotlin.io.path.*

private var firstRunLock: Boolean? = null

public val Server.isFirstRun: Boolean get() {
    return firstRunLock ?: (!Path("EULA.txt").exists()).also { firstRunLock = it }
}

internal fun Server.genServerFiles() {
    val eulaFile = Path("EULA.txt")
    val eula = resourceAsBytes("defaults/EULA.txt")
    eulaFile.takeIf { !it.exists() }?.writeBytes(eula)

    // val settingsFile = Path("settings.server.kts")
    // val settings = resourceAsBytes("defaults/settings.server.kts")
    // settingsFile.takeIf { !it.exists() }?.writeBytes(settings)
    
    val iconFile = Path("icon.jpeg")
    val icon = resourceAsBytes("defaults/icon.jpeg")
    iconFile.takeIf { !it.exists() }?.writeBytes(icon)


    Path("plugins").also {
        if (!it.exists()) {
            it.createDirectory()
        }
    }
}

internal fun Server.eulaPrompt() {
    if (!isEulaSigned()) {
        println("The Minecraft EULA (EULA.txt) must be agreed upon to continue.")
        println("Type [yes] if you agree to follow the terms of the EULA...")
        val response = readln()
        if (response.equals("yes", true) || response.equals("y", true)) {
            signEula()
        } else {
            exitProcessWithMessage("EULA must be signed to continue.", 1)
        }
    }
}

private fun isEulaSigned(): Boolean {
    val eulaFile = Path("EULA.txt")
    return (eulaFile.bufferedReader().use { it.readLine().startsWith("Agreed upon at") })
}

private fun signEula() {
    val eulaFile = Path("EULA.txt")
    val eula = resourceAsBytes("defaults/EULA.txt")
    val signedEula = "Agreed upon at ${Date()}.\n\n$eula"
    eulaFile.writeText(signedEula)
}