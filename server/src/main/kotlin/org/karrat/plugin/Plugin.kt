/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.plugin

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Plugin(val name: String, val version: String)