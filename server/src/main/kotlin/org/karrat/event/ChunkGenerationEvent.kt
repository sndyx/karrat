/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.event

import org.karrat.World
import org.karrat.struct.Vec2i

public data class ChunkGenerationEvent(
    public val world: World,
    public val chunk: Vec2i,
) : CancellableEvent()