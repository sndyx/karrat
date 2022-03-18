/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.codegen.utils

import kotlin.io.path.*

fun file(path: String, block: FileScope.() -> Unit) {
    val scope = FileScope()
    scope.block()
    val content = scope.build()
    val file = Path("../$path")
    println(file.absolutePathString())
    if (file.exists()) {
        if (file.readText() == content) {
            return
        }
    }
    file.writeText(content)
}

class FileScope {

    private var pkg: String = ""
    private val imports: MutableList<String> = mutableListOf()
    private val content = StringBuilder()
    private var indent = 0

    fun pkg(value: String) {
        pkg = value
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
        result.append(" * Copyright © Karrat - 2021.\n")
        result.append(" */\n\n")
        result.append("package ").append(pkg).append("\n\n")
        imports.forEach {
            result.append("import ").append(it).append("\n")
        }
        if (imports.isNotEmpty()) {
            result.append("\n")
        }
        result.append("// Auto-generated file. DO NOT EDIT!\n\n")
        result.append(content.toString())
        return result.toString()
    }

}