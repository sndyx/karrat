# Karrat Minecraft server

Welcome to [Karrat](https://karrat.org/)!
It is a quick, open-source Minecraft server implementation developed solely by
 [Sndy](https://sndy.moe/) and open-source contributors.

* Simple and easy to install.
* Greatly improves developer resources and [plugin API](#developers).
  * No more decompiling and deobfuscating Minecraft jars.
* Might make your dad come back from the gas station!!! (No guarantees)

## Table of contents

<!--- TOC -->

* [Installation](#installation)
* [Developers](#developers)
  * [Setup](#setup)
    * [Gradle](#gradle)
    * [Maven](#maven-not-recommended)
  * [Documentation](#documentation)

<!--- END -->

## Installation

TODO

## Developers

Karrat is designed to be as concise, clean, and friendly as possible. Its
 API is meticulously optimized to be very beginner-friendly and easy to work
 with, while maintaining a ridiculously customizable core.

### Setup

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

## Documentation

TODO

# License

Karrat is distributed under the terms of the GNU Lesser General Public License v3.0. See [LICENSE.txt](LICENSE.txt) for the full license.

# Contributing

Please be sure to review Karrat's [contributing guidelines](docs/contributing.md) to learn how to help the project.