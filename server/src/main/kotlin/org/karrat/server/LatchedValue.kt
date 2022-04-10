/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

/**
 * Specifies that the annotated property is post-init-immutable. After the
 * server fully starts, and clients can begin to join, attempting to set this
 * property will result in an exception.
 */
@Target(FIELD, PROPERTY)
@Retention(BINARY)
@MustBeDocumented
public annotation class LatchedValue
