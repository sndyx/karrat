/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.script

import org.karrat.Config
import java.nio.file.Path
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate
import kotlin.script.experimental.jvmhost.createJvmEvaluationConfigurationFromTemplate

@KotlinScript(
    fileExtension = "server.kts",
    compilationConfiguration = SettingsScriptCompilationConfiguration::class,
    evaluationConfiguration = SettingsScriptEvaluationConfiguration::class,
)
public abstract class ServerSettings

internal object SettingsScriptCompilationConfiguration : ScriptCompilationConfiguration({
    defaultImports(Config::class)
    implicitReceivers(Config::class)
    jvm {
        dependenciesFromCurrentContext(wholeClasspath = true)
    }
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
})

internal object SettingsScriptEvaluationConfiguration : ScriptEvaluationConfiguration({
    implicitReceivers(Config)
})

public fun runSettingsScript(path: Path): ResultWithDiagnostics<EvaluationResult> {
    return BasicJvmScriptingHost().eval(
        path.toFile().toScriptSource(),
        createJvmCompilationConfigurationFromTemplate<ServerSettings>(),
        createJvmEvaluationConfigurationFromTemplate<ServerSettings>()
    )
}