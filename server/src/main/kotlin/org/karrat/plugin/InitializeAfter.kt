/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.plugin

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class InitializeAfter(vararg val after: String)
