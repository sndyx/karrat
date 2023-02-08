/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.plugin

import org.karrat.Server
import org.karrat.struct.ByteBuffer
import org.karrat.struct.readBytes
import org.karrat.struct.readUShort
import org.karrat.struct.toByteBuffer
import java.net.URLClassLoader
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.io.path.readBytes

public abstract class PluginClassLoader : ClassLoader() {

    public abstract val pluginClass: PluginClass

    public companion object {

        /**
         * Resolves the class within the plugin [jar file][bytes] annotated with the
         * [Plugin] annotation.
         *
         * @return plugin class name.
         * @throws PluginInitializationException if no annotated class could be found.
         */
        public fun findPluginClass(bytes: ByteArray): Result<String> {
            ZipInputStream(bytes.inputStream()).use { zip ->
                var entry: ZipEntry?
                while (zip.nextEntry.also { entry = it } != null) {
                    if (entry!!.isDirectory || !entry!!.name.endsWith(".class")) continue
                    pluginClassOrNull(zip.readBytes().toByteBuffer())?.let {
                        return Result.success(it.replace('/', '.'))
                    }
                }
            }
            return Result.failure(PluginInitializationException("Plugin for jar could not be resolved."))
        }

        /**
         * Determines whether a class is annotated with the [Plugin] annotation from
         * its bytecode.
         *
         * @return internal class name, or `null` if not annotated with `Plugin`.
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

    }

}

public class UrlPluginClassLoader(jar: Path) : PluginClassLoader() {

    private val loader = URLClassLoader.newInstance(
        arrayOf(jar.toUri().toURL()),
        Server::class.java.classLoader
    )
    private val className = findPluginClass(jar.readBytes()).getOrThrow()

    override val pluginClass: PluginClass =
        PluginClass(Class.forName(className, true, loader))

}