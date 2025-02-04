/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.event

import org.karrat.entity.Player
import org.karrat.play.Container

public class PlayerCloseContainerEvent(
    public val player: Player,
    public val container: Container
) : Event