/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

@DslMarker
@Target(CLASS, TYPE)
@Retention(BINARY)
public annotation class CommandDsl