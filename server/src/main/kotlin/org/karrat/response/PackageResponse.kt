/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.response

import kotlinx.serialization.Serializable

@Serializable
public class PackageResponse(
    public val size: Double,
    public val dependencies: Array<String>
)