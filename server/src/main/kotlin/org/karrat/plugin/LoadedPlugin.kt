/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

public class LoadedPlugin(public val pluginClass: Class<*>) : Comparable<LoadedPlugin> {
    
    public val pluginAnnotation: Plugin = pluginClass.annotations.first { it is Plugin } as Plugin
    public val dependsOnAnnotation: DependsOn? = pluginClass.annotations.find { it is DependsOn } as? DependsOn
    
    public val id: String = pluginAnnotation.id
    public val name: String = pluginAnnotation.name
    public val version: String = pluginAnnotation.version
    public val dependsOn: List<String>? = dependsOnAnnotation?.plugins?.toList()
    
    public fun init() {
        pluginClass.declaredMethods.find { f -> f.declaredAnnotations.any { it is Init } }?.invoke(null)
    }
    
    override fun compareTo(other: LoadedPlugin): Int {
        if (dependsOn?.contains(other.id) == true) return 1
        if (other.dependsOn?.contains(id) == true) return -1
        return 0
    }
    
}

