/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import org.karrat.Server
import org.karrat.server.parallelize
import org.karrat.struct.*
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.*
import kotlin.system.measureTimeMillis

internal suspend fun Server.loadPlugins() {
    val path = Path("plugins")
    if (!path.exists()) return
    @Suppress("BlockingMethodInNonBlockingContext")
    val jars = path.listDirectoryEntries().filter { it.extension == "jar" }
    Server.parallelize(jars) { jar ->
        runCatching {
            var plugin: LoadedPlugin
            val time = measureTimeMillis {
                plugin = loadPlugin(jar)
            }
            println("Loaded plugin ${plugin.name} in ${time}ms.")
        }.onFailure {
            println("Failed to load plugin for jar ${jar.name}.")
        }
    }
    plugins.sorted().forEach {
        it.init()
    }
}

public fun Server.loadPlugin(jar: Path): LoadedPlugin {
    val pluginClassName = resolvePluginClass(jar)
    val loader = URLClassLoader.newInstance(
        arrayOf(jar.toUri().toURL()),
        Server::class.java.classLoader
    )
    val instance = Class.forName(pluginClassName, true, loader)
    return LoadedPlugin(instance)
}

/**
 * Resolves the class within the plugin [jar file][jar] annotated with the
 * [Plugin] annotation.
 *
 * @return plugin class name.
 * @throws PluginInitializationException if no annotated class could be found.
 */
private fun resolvePluginClass(jar: Path): String {
    ZipInputStream(jar.inputStream()).use { zip ->
        var entry: ZipEntry
        while (zip.nextEntry.also { entry = it } != null) {
            if (entry.isDirectory || !entry.name.endsWith(".class")) continue
            pluginClassOrNull(zip.readBytes().toByteBuffer())?.let {
                return it.replace('/', '.')
            }
        }
    }
    throw PluginInitializationException("Plugin for jar: '${jar.name}' could not be resolved.")
}

/**
 * Determines whether a class is annotated with the [Plugin] annotation from
 * its bytecode.
 *
 * @return class name, or `null` if not annotated with `Plugin`.
 */
private fun pluginClassOrNull(data: ByteBuffer): String? = data.run {
    skip(8)
    val poolSize = readShort()
    val utf8Pool = mutableMapOf<Int, String>()
    val classPool = mutableMapOf<Int, UShort>()
    repeat(poolSize.toInt() - 1) {
        when (read().toInt()) {
            1 -> utf8Pool[it + 1] = readBytes(readUShort().toInt()).decodeToString()
            7 -> classPool[it + 1] = readUShort()
            8, 16, 19, 20 -> skip(2)
            15 -> skip(3)
            3, 4, 9, 10, 11, 12, 17, 18 -> skip(4)
            5, 6 -> skip(8)
        }
    }
    skip(2)
    val thisClass = readUShort().toInt()
    skip(2)
    skip(readUShort().toInt() * 2)
    repeat(2) {
        repeat(readUShort().toInt()) {
            skip(6)
            val attributeCount = readUShort()
            repeat(attributeCount.toInt()) {
                skip(2)
                skip(readInt())
            }
        }
    }
    val attributeCount = readUShort().toInt()
    repeat(attributeCount) {
        val aNameIndex = readUShort()
        val attributeName = utf8Pool[aNameIndex.toInt()]
        if (attributeName == "RuntimeVisibleAnnotations") {
            skip(4)
            val annotationCount = readUShort().toInt()
            repeat(annotationCount) {
                val type = utf8Pool[readUShort().toInt()]
                if (type == "Lorg/karrat/plugin/Plugin;") return utf8Pool[classPool[thisClass]!!.toInt()]
            }
        } else {
            skip(readInt())
        }
    }
    return null
}
