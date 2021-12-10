/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event.instances

import org.karrat.event.CancellableEvent
import org.karrat.network.Session
import org.karrat.network.entity.StatusResponse

/*
 * [Event] When a Status Response (Server Info) is sent to a client
 *
 * Parameters
 * - [Session] session, relevant session
 * - [c] [StatusResponse] response, relavant Status Response
 *
 * Can be Cancelled
 */
public class StatusResponseEvent(
    public val session: Session,
    public var response: StatusResponse
) : CancellableEvent() {
    
    override fun toString(): String =
        "StatusResponseEvent(session=$session, response=$response)"
    
}