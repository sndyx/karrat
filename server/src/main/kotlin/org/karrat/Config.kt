/*
 * Copyright © Karrat - 2022.
 */

package org.karrat

import org.karrat.play.colored
import org.karrat.server.LatchedValue
import org.karrat.struct.Location
import org.karrat.struct.Uuid
import org.karrat.struct.id
import kotlin.math.max
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public object Config {
    
    @PublishedApi
    internal var lock: Boolean = false
    
    // Constants
    
    /**
     * The current version of Minecraft.
     */
    public const val versionName: String = "1.18.2"

    /**
     * The protocol version number used by the current version of Minecraft.
     */
    public const val protocolVersion: Int = 758
    
    /**
     * The session server used by [Server.auth] to authenticate players.
     */
    public const val sessionServer: String = "https://sessionserver.mojang.com"
    
    // Meta
    
    /**
     * The id of the main server thread.
     */
    public var mainThreadId: Long = Thread.currentThread().id
    
    /**
     * The amount of threads the server should use. Defaults to the number of
     * available processors. Must be at least two.
     */
    @LatchedValue
    public var threadCount: Int by latched(vetoable(max(2, Runtime.getRuntime().availableProcessors())) { it > 2 })
    
    /**
     * Whether the server should use a basic logger. By default, the server sets
     * [System.out] to a more resource-intensive PrintStream. Enabling basic
     * logging can increase server speeds.
     */
    @LatchedValue
    public var basicLogging: Boolean by latched { false }
    
    @LatchedValue
    public var isDevEnvironment: Boolean by latched { false }
    
    // Network
    
    /**
     * The port on which the server will run. Defaults to 25565.
     */
    @LatchedValue
    public var port: Int by latched { 25565 }
    
    /**
     * The duration clients will have to wait before reconnecting.
     */
    public var connectionThrottle: Duration = 2.seconds
    
    /**
     * The network compression threshold, in bytes.
     */
    public var compressionThreshold: Int by positive(128)
    
    /**
     * The maximum capacity of a network buffer, in bytes.
     */
    public var networkBufferSize: Int by positive(32767)
    
    /**
     * Whether proxy connections should be refused.
     */
    public var preventProxyConnections: Boolean = false
    
    // In-game
    
    /**
     * The [Location] players will spawn into by default.
     */
    public val spawnLocation: Location by lazy { Location(World(id("minecraft:Main_World")), 0.0, 0.0, 0.0) }
    
    // Behavior
    
    /**
     * The number of times the server will tick in a second.
     */
    public var tickrate: Int by positive(20)
    
    /**
     * The number of chunks around a given player to be sent to the client.
     */
    public var viewDistance: Int by positive(8)
    
    /**
     * The number of chunks around a given player to be simulated.
     */
    public var simulationDistance: Int by positive(8)
    
    // Administration
    
    /**
     * Whether the server is whitelist only. If true, players' uuids must be
     * included in the [whitelist] to join.
     */
    public var isWhitelistOnly: Boolean = false
    
    /**
     * A list of uuids corresponding to the players who are allowed to join the
     * server.
     */
    public var whitelist: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79a"))
    
    /**
     * A list of uuids corresponding to the players who are banned from the
     * server.
     */
    public var bannedPlayers: MutableList<Uuid> = mutableListOf(Uuid("bf8c0810-3dda-48ec-a573-43e162c0e79b"))
    
    /**
     * The maximum amount of players that can join before connections will be
     * refused.
     */
    public var maxPlayers: Int by positive(100)
    
    // Display
    
    /**
     * The text displayed on the client's server list.
     */
    public var motd: String = "Hello Kevster109"
    
    private var legacyMotdValue: String? = null
    
    /**
     * The [motd] displayed to any client below Minecraft version 1.6. If not
     * specified, will use the regular [motd].
     */
    public var legacyMotd: String
    get() = legacyMotdValue ?: motd
    set(value) {
        legacyMotdValue = value
    }
    
    // Command
    
    /**
     * Whether command capitalization should be ignored.
     */
    public var ignoreCommandCapitalization: Boolean = true
    
    /**
     * The message sent to the client upon the run of an unknown command.
     */
    public var unknownCommandMessage: String = "&cUnknown command.".colored()
    
    /**
     * The message sent to the client when a command is run with invalid syntax.
     */
    public var invalidSyntaxMessage: String = "&cInvalid syntax.".colored()
    
    public inline fun <T> latched(initializer: () -> T): ReadWriteProperty<Any?, T> =
        Delegates.vetoable(initializer.invoke()) { property, _, _ ->
            if (lock) throw IllegalStateException("Cannot modify post-init-immutable variable ${property::name} after initialization.") else true
        }
    
    public fun <T> latched(property: ReadWriteProperty<Any?, T>): ReadWriteProperty<Any?, T> =
        LatchWrapper(property)
    
    private class LatchWrapper<T>(val wrapped: ReadWriteProperty<Any?, T>) : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T = wrapped.getValue(thisRef, property)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (lock) throw IllegalStateException("Cannot modify post-init-immutable variable ${property::name} after initialization.")
            wrapped.setValue(thisRef, property, value)
        }
    }
    
    public inline fun <T : Number> vetoable(
        value: T,
        crossinline onChange: (newValue: T) -> Boolean
    ): ReadWriteProperty<Any?, T> =
        Delegates.vetoable(value) { _, _, newValue -> onChange.invoke(newValue) }
    
    public fun <T : Number> positive(value: T): ReadWriteProperty<Any?, T> =
        vetoable(value) {
            it.toDouble() > 0
        }
    
}
