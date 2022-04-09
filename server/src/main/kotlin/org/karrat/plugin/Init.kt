/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.plugin

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

@Target(FUNCTION)
@Retention(RUNTIME)
public annotation class Init