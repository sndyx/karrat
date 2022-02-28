/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
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