/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.event

import org.karrat.network.Session
import org.karrat.network.entity.StatusResponse

/**
 * A [CancellableEvent] fired when a [StatusResponse] is sent to the client.
 * When cancelled, the player will not receive any relevant information on the
 * server's status.
 */
public class StatusResponseEvent(
    public val session: Session,
    public var response: StatusResponse
) : CancellableEvent() {

    override fun toString(): String =
        "StatusResponseEvent(session=$session, response=$response)"

}