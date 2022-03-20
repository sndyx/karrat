/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.karrat.struct.Uuid

@Serializable
public data class SessionServerResponse(
    @SerialName("id")
    val uuid: Uuid,
    val name: String,
    val properties: List<SessionServerProperty>
)

@Serializable
public data class SessionServerProperty(
    val name: String,
    val value: String,
    val signature: String,
)