/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.packet

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Clientbound

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Serverbound
