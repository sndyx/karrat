Docs / Concepts / [Events](events.md)

---

# Events

An *event* is a mutable representation of an action happening on the server. When its values are changed, the server will use the new values in the action.

```kotlin
@Init
fun init() {
	Server.on<JoinGameEvent> { // it: JoinGameEvent
		println("${it.name} joined the game!")
	}
}
```
[//]: # (3..5)

The code inside `Server.on<T> { ... }` is run whenever an event matching type `T` is fired.

This behavior can also be extracted into a function:

```kotlin
@Init
fun init() {
	Server.on(::onJoinGame)
}

fun onJoinGame(event: JoinGameEvent) { ... } // Event must be only parameter

```

Almost every server action has its own events, which makes it exceptionally easy to override server behavior.

## Event Annotation

Just like the convenient `Init` and `Exit` annotations, events can be declared with the `Event` annotation.

```kotlin
@Event
fun onJoinGame(event: JoinGameEvent) {
	println("${it.name} joined the game!")
}
```

Thats it! There is no need to register or point to this function, Karrat will automatically search for them when the plugin is loaded.

While the `Event` annotation is conceptually similar to the `Init` and `Exit` annotations, there is one important distinction between them. Events using the `Event` annotation are not bound to the main plugin file.

## Custom Events

Sometimes you might want to create your own events to organize actions or provide a means of interacting with your plugin. This can be done by implementing `Event` or `CancellableEvent`.

```kotlin
public data class SpellCastEvent(  
    public val player: Player,  
    public val spell: Spell,  
) : CancellableEvent()
```

now whenever the action this event represents occurs, you can fire the event using `Server.dispatchEvent`

```kotlin
fun Player.castLightningSpell(velocity: Vec3d) {
	val event = SpellCastEvent(this, Spell.Lightning(velocity))
	dispatchEvent(event) // Fire event
	if (event.isCancelled) return // Check if any consumers cancelled the event
	// Run code...
	playSpell(event.spell)
}
```

---

Next: [Worlds](worlds.md)