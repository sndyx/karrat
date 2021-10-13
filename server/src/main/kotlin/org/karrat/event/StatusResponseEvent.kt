/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.network.entity.StatusResponse

/**
 * Event called when a Status Response is sent to the client
 */
public class StatusResponseEvent(
    public val session: Session,
    public var response: StatusResponse
) : CancellableEvent() {
    
    override fun toString(): String =
        "StatusResponseEvent(session=$session, response=$response)"
    
}