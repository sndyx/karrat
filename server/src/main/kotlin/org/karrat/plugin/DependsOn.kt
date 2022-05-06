/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@Target(CLASS, FILE)
@Retention(RUNTIME)
public annotation class DependsOn(vararg val after: String)
