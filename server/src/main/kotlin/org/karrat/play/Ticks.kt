/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlin.time.Duration

public val Duration.inWholeTicks: Long get() =
    this.inWholeSeconds * 20