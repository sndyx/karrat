/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.entity

import org.karrat.play.Location

public abstract class Entity {
    public lateinit var location : Location;

    public open fun remove() {
    
    }
    
}