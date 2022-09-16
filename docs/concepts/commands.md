Docs / Concepts / [Commands](commands.md)

---

# Commands

Commands are used by players and administrators to interact with the server in more complex ways.

## Running a Command

`Command.run` runs the given command `String`.

```
@Init
fun init() {
	Command.run("kill @a")
}
```
[//]: # (3..5)

```kotlin
command("kill") {
	argument<Player>().onRun {
		it.remove()
	}
}
```