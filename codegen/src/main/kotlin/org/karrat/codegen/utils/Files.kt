/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.codegen.utils

import kotlin.io.path.*

fun file(path: String, block: FileScope.() -> Unit) {
    val scope = FileScope(path)
    scope.block()
    val content = scope.build()
    val file = Path("../$path")

    if (file.exists() && file.readText() == content) {
        return
    }

    file.writeText(content)
}

open class FileScope(path: String) {

    private var generator: String = ""
    private var source: String = ""
    private val imports: MutableList<String> = mutableListOf()
    private val content = StringBuilder()
    private var indent = 0
    private val pkg: String

    init {
        pkg = path.substringAfter("src/main/kotlin/").substringBeforeLast("/").replace("/", ".")
    }

    fun generator(value: String) {
        generator = value
    }

    fun source(value: String) {
        source = value
    }

    fun import(value: String) {
        imports.add(value)
    }

    operator fun String.unaryPlus() {
        content.append(
            " ".repeat(indent * 4) + replace(
                "\n",
                "\n" + " ".repeat(indent * 4)
            ) + "\n"
        )
    }

    fun indent(block: () -> Unit) {
        pushIndent()
        block()
        popIndent()
    }

    private fun pushIndent() {
        indent++
    }

    private fun popIndent() {
        indent--
    }

    fun build(): String {
        val result = StringBuilder()
        result.append("/*\n")
        result.append(" * Copyright © Karrat - 2022.\n")
        result.append(" */\n\n")
        result.append("package ").append(pkg).append("\n\n")
        imports.forEach {
            result.append("import ").append(it).append("\n")
        }
        if (imports.isNotEmpty()) {
            result.append("\n")
        }
        result.append("/*\n")
        result.append(" * File auto-generated by `${generator}`\n")
        if (source.isNotEmpty()) {
            result.append(" * from `${source}` source file.\n")
        }
        result.append(" * DO NOT EDIT!\n")
        result.append(" */\n\n")
        result.append(content.toString())
        return result.toString()
    }
}