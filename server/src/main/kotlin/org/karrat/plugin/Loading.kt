/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.karrat.Server
import org.karrat.server.parallelize
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readBytes
import org.karrat.struct.toByteBuffer
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.*

internal fun Server.loadPlugins() {
    @Suppress("BlockingMethodInNonBlockingContext")
    val jars = Path("plugins").listDirectoryEntries().filter { it.extension == "jar" }
    jars.forEach {
        println("Loading plugin: ${it.nameWithoutExtension}.")
        runCatching { loadPlugin(it) }
            .onSuccess { p -> plugins.add(p) }
            .onFailure { e -> e.printStackTrace() }
    }
    plugins.forEach {
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
    return LoadedPlugin(instance.kotlin)
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
                return it
            }
        }
    }
    throw PluginInitializationException("Plugin class for: ${jar.nameWithoutExtension} could not be resolved.")
}

/**
 * Determines whether a class is annotated with the [Plugin] annotation from
 * its bytecode.
 *
 * @return class name, or `null` if not annotated with `Plugin`.
 */
private fun pluginClassOrNull(data: ByteBuffer): String? = data.run {
    skip(8)
    val cpSize = readShort()
    val constants = buildMap<Int, Any> {
        repeat(cpSize.toInt()) {
            when (read().toInt()) {
                1 -> set(it + 1, readBytes(readShort().toInt()).decodeToString())
                7 -> set(it + 1, readShort())
                8, 16, 19, 20 -> skip(2)
                15 -> skip(3)
                3, 4, 9, 10, 11, 12, 17, 18 -> skip(4)
                5, 6 -> skip(8)
            }
        }
    }
    skip(2)
    val classIndex = readShort()
    skip(2)
    skip(readShort() * 2)
    repeat(2) {
        repeat(readShort().toInt()) {
            skip(6)
            val aCount = readShort()
            repeat(aCount.toInt()) {
                skip(2)
                skip(readInt())
            }
        }
    }
    val aCount = readShort()
    repeat(aCount.toInt()) {
        val aNameIndex = readShort()
        val attributeName = constants[aNameIndex.toInt()]
        if (attributeName == "RuntimeVisibleAnnotations") {
            skip(4)
            val count = readShort()
            repeat(count.toInt()) {
                val type = constants[readShort().toInt()]
                if (type == "Lorg/karrat/plugin/Plugin;")
                    return constants[(constants[classIndex.toInt()] as Short).toInt()] as String
            }
        } else {
            skip(readInt())
        }
    }
    return null
}