/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.entity

import org.karrat.play.Location

public abstract class Entity(public var location: Location) {

    public open fun remove() {

    }

}