/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.entity

import org.karrat.struct.Uuid

/**
 * Returns the first [Player] matching the given [name], or null if element was
 * not found.
 */
public fun Collection<Player>.getByNameOrNull(name: String): Player? =
    firstOrNull { it.name.equals(name, ignoreCase = true) }

/**
 * Retrieves the [Player] matching the given [name].
 */
public fun Collection<Player>.getByName(name: String): Player =
    getByNameOrNull(name) ?: throw NoSuchElementException("Collection contains no player matching the given name.")

/**
 * Returns the first [Player] matching the given [uuid], or null if element was
 * not found.
 */
public fun Collection<Player>.getByUuidOrNull(uuid: Uuid): Player? =
    firstOrNull { it.uuid == uuid }

/**
 * Retrieves the [Player] matching the given [uuid].
 */
public fun Collection<Player>.getByUuid(uuid: Uuid): Player =
    getByUuidOrNull(uuid) ?: throw NoSuchElementException("Collection contains no player matching the given Uuid.")

/**
 * Returns the first [Entity] matching the given [id], or null if element was
 * not found.
 */
public fun <T : Entity> Collection<T>.getByIdOrNull(id: Int): T? =
    firstOrNull { it.id == id }

/**
 * Returns the first [Entity] matching the given [id].
 */
public fun <T : Entity> Collection<T>.getById(id: Int): T =
    getByIdOrNull(id) ?: throw NoSuchElementException("Collection contains no entity matching the given id.")