/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

public class PluginClass(public val java: Class<*>) : Comparable<PluginClass> {

    public val plugin: Plugin = java.annotations.first { it is Plugin } as Plugin
    public val dependsOn: DependsOn? = java.annotations.find { it is DependsOn } as? DependsOn

    public fun init() {
        val function = java.declaredMethods.find { f ->
            f.declaredAnnotations.any { it is Init }
        } ?: return
        if (function.parameterCount == 0) function.invoke(null)
        else if (function.parameterCount == 1
            && function.parameters[0].type == Plugin::class.java
        ) function.invoke(null, plugin)
        else throw PluginInitializationException("Plugin init function must not have parameters.")
    }

    public fun exit() {
        val function = java.declaredMethods.find { f ->
            f.declaredAnnotations.any { it is Exit }
        } ?: return
        if (function.parameterCount == 0) function.invoke(null)
        else if (function.parameterCount == 1
            && function.parameters[0].type == Plugin::class.java
        ) function.invoke(null, plugin)
        else throw PluginException("Plugin exit function must not have parameters.")
    }

    override fun compareTo(other: PluginClass): Int {
        if (dependsOn?.plugins?.any { it == other.plugin.id } == true) return 1
        if (other.dependsOn?.plugins?.any { it == plugin.id } == true) return -1
        return 0
    }

}