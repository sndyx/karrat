/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.server

@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "This is a delicate API and its use requires care." +
            " Make sure you fully read and understand documentation of the declaration that is marked as a delicate API."
)
public annotation class DelicateServerApi
