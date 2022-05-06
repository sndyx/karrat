/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.configuration

import org.karrat.Server
import org.karrat.internal.exitProcessWithMessage
import java.util.*
import kotlin.io.path.*

private var firstRunLock: Boolean? = null

public val Server.isFirstRun: Boolean get() {
    return firstRunLock ?: (!Path("EULA.txt").exists()).also { firstRunLock = it }
}

internal fun Server.genServerFiles() {
    val eulaFile = Path("EULA.txt")
    val eula = getResource("defaults/EULA.txt")
    eulaFile.takeIf { !it.exists() }?.writeText(eula)

    val settingsFile = Path("settings.server.kts")
    val settings = getResource("defaults/settings.server.kts")
    settingsFile.takeIf { !it.exists() }?.writeText(settings)
}

internal fun Server.eulaPrompt() {
    if (!isEulaSigned()) {
        println("The Minecraft EULA (EULA.txt) must be agreed upon to continue.")
        println("Type [yes] if you agree to follow the terms of the EULA...")
        val response = readln()
        if (response.equals("yes", true) || response.equals("y", true)) {
            signEula()
        } else {
            exitProcessWithMessage("You need to sign the EULA", 1)
        }
    }
}

private fun isEulaSigned(): Boolean {
    val eulaFile = Path("EULA.txt")
    return (eulaFile.bufferedReader().use { it.readLine().startsWith("Agreed upon at") })
}

private fun signEula() {
    val eulaFile = Path("EULA.txt")
    val eula = getResource("defaults/EULA.txt")
    val signedEula = "Agreed upon at ${Date()}.\n\n$eula"
    eulaFile.writeText(signedEula)
}

private fun getResource(path: String): String =
    Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream(path)!!
        .readAllBytes()
        .decodeToString()