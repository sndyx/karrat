# TODO

A helpful list of things that need to be completed. Anything that isn't obvious
 or required to make the server work should be put in the
 [experimental ideas](#experimental-ideas) category. For important work that
 needs to be completed in a timely manner, see [this section](#soon).

## Soon

Tasks that should take priority, ordered by importance.

---

* Setup basic play packets so that players can join the world
* Complete entity framework
* Complete world-related tasks

## Misc

Tasks that can be done whenever. If you aren't inspired to do anything or are
 stuck on your current task, consider working on one of these.

---

* Come up with intuitive item NBT system

## Later

Tasks that should be kept in mind but require prerequisites or would not be
 of any use so early on in the development period.

---

...

## Experimental Ideas

Ideas that might make the developer experience more convenient (without
 sacrificing performance) that don't necessarily have to be added for the server
 to properly work. Ideas here do not have to be tasks. They can be as simple or
 complex as you want, as long as you get your point across well.
>An example of this would be the `Nbt.kt` serialization module. It is a
> convenient and concise way to translate data to NBT using 
> `kotlinx.serialization` without sacrificing code readability or performance.

* Annotation-powered compiler plugin that automatically registers functions 
 annotated with `@Subscribe` to the event bus

* Domain-based plugin repository allowing players to quickly install plugins via
 `/plugin install <namespace>`
> An example of this format: `/plugin install org.karrat.plugins.ranks`.
>  "Featured plugins" (quality handpicked plugins) would also be provided with 
>  shorthand names such as: `/plugin install ranks`.

* `org.karrat.context`, a library for advanced clientside manipulation.