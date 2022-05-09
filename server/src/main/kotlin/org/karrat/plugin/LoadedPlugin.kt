/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions

public class LoadedPlugin(public val pluginClass: Class<*>) {
    
    public val pluginAnnotation: Plugin = pluginClass.annotations.first { it is Plugin } as Plugin
    public val dependsOnAnnotation: DependsOn? = pluginClass.annotations.find { it is DependsOn } as? DependsOn
    
    public val name: String = pluginAnnotation.name
    public val version: String = pluginAnnotation.version
    public val dependsOn: List<String>? = dependsOnAnnotation?.plugins?.toList()
    
    public fun init() {
        pluginClass.declaredMethods.find { f -> f.declaredAnnotations.any { it is Init } }?.invoke(null)
    }
    
}

