/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.plugin

import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.karrat.Server
import org.karrat.server.Registry
import org.karrat.server.parallelize
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.system.measureTimeMillis

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Plugin(val id: String, val name: String, val version: String) {

    public companion object PluginRegistry : Registry<Plugin> {

        public val classLoaders: MutableList<PluginClassLoader> = mutableListOf()
        override val list: MutableList<Plugin> = mutableListOf()

        override fun load(): Unit = runBlocking {
            val path = Path("plugins")
            if (!path.exists()) return@runBlocking
            val jars = path.listDirectoryEntries().filter { it.extension == "jar" }
            Server.parallelize(jars) { jar ->
                runCatching {
                    var loaded: Plugin
                    val time = measureTimeMillis {
                        loaded = load(jar)
                    }
                    println("Loaded plugin ${loaded.name} in ${time}ms.")
                }.onFailure {
                    println("Failed to load plugin for jar '${jar.name}'.")
                }
            }.awaitAll()
            list.forEach { plugin ->
                val missing = plugin.dependants.filter { id -> list.none { it.id == id } }
                if (missing.isNotEmpty()) println("Missing dependencies for ${plugin.name}: $missing.\nThis plugin may not work correctly.")
                runCatching {
                    plugin.init()
                }.onFailure {
                    PluginInitializationException("Failed to initialize plugin ${plugin.name} (${plugin.id}).", cause = it).printStackTrace()
                }
            }
        }

        public fun load(jar: Path): Plugin = load(UrlPluginClassLoader(jar))
        public fun load(loader: PluginClassLoader): Plugin {
            classLoaders.add(loader)
            list.add(loader.pluginClass.plugin)
            return loader.pluginClass.plugin
        }

        public fun unload(id: String): Boolean {
            return classLoaders.find { it.pluginClass.plugin.id == id }?.let {
                list.remove(it.pluginClass.plugin)
                classLoaders.remove(it)
            } != null
        }

        context(Plugin)
        override fun unregister(value: Plugin) {
            list.remove(value)
        }

        context(Plugin)
        override fun register(value: Plugin) {
            list.add(value)
        }

    }

}