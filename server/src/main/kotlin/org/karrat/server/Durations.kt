/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.server

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

public val Int.ticks: Duration get() = this.times(50).milliseconds

public val Duration.inWholeTicks: Long get() =
    this.inWholeSeconds * 20