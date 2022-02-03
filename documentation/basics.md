# Basics

## Setup

Using the Karrat API requires Kotlin compiler `1.5.0` or higher.

### Gradle

To set up your plugin development environment, simply add the artifact to your
 `build.gradle`(`.kts`) file.

Kotlin DSL:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.karrat:karrat-server-plugin:1.17.1")
}
```

Groovy DSL:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "org.karrat:karrat-server-plugin:1.17.1"
}
```

>We also provide the `karrat-server` artifact that contains all the essential server code but does not have plugin support bundled with it.

---

### Maven (Not Recommended)

To set up your plugin development environment, simply add the artifact to your
 `pom.xml` file.

```xml
<dependency>
    <groupId>org.karrat</groupId>
    <artifactId>karrat-server-plugin</artifactId>
    <version>1.17.1</version>
</dependency>
```

## Entry Point

Your plugin should be intialized via Kotlin's init block.

```kt
object ExamplePlugin : Plugin {

    init {
        println("Plugin initialized.")
    }

}
```
