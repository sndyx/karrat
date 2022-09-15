/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.network.auth

import java.security.PublicKey

public class MessageKeyInfo(public val key: PublicKey, public val signature: ByteArray, public val expiresAt: Long)
