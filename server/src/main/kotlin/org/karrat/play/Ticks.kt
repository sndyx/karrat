/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

import kotlin.time.Duration

public val Duration.inWholeTicks: Long get() =
    this.inWholeSeconds * 20