/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Plugin(val name: String, val version: String)
