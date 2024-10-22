# Karrat Minecraft server
![image](https://user-images.githubusercontent.com/70289658/135740635-be07452e-614a-4416-bfe0-e7354153b176.png)

Welcome to Karrat!
It is a quick, open-source Minecraft server implementation developed by
 [Sndy](https://sndy.moe/) and open-source contributors.

* Simple and easy to install.
* Greatly improves developer resources and [plugin API](#developers).
  * No more decompiling and deobfuscating Minecraft jars.
* Might make your dad come back from the gas station!!! (No guarantees)

## Table of contents

<!--- TOC -->

- [Installation](#installation)
- [Developers](#developers)
  - [Setup](#setup)
    - [Gradle](#gradle)
    - [Maven](#maven-not-recommended)
  - [Documentation](#documentation)
- [Credits](#credits)
- [License](#license)
- [Contributing](#contributing)

<!--- END -->

## Installation

To install Karrat, simply replace your `server.jar` file with the Karrat jar file.
 You must also conform to the [Karrat run arguments](/todo).

## Developers

Karrat is designed to be as concise, clean, and friendly as possible. Its
 API is meticulously optimized to be very beginner-friendly and easy to work
 with, while maintaining a ridiculously customizable core.

### Setup

Using the Karrat API requires Kotlin compiler `1.6.0` or higher.

### Gradle

To set up your plugin development environment, add the artifact to your
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
    implementation 'org.karrat:karrat-server-plugin:1.17.1'
}
```

>We also provide the `karrat-server` artifact that contains all the essential server code but does not have plugin support bundled with it.

---

### Maven

To set up your plugin development environment, add the artifact to your
 `pom.xml` file.

```xml
<dependency>
    <groupId>org.karrat</groupId>
    <artifactId>karrat-server-plugin</artifactId>
    <version>1.17.1</version>
</dependency>
```

## Documentation

Documentation can be found [here](/documentation/introduction.md).

# Credits

This project is only possible due to the hard work put in by members of
 the Minecraft community!

Credits to *wiki.vg* for the great amount of help provided through its protocol information.

The following files are used in this program:
 - *dimension_codec.json*, courtesy of *wiki.vg*.
 - *materials.json*, courtesy of *prismarine.js*
 - *blocks.json*, courtesy of *prismarine.js*

# License

Karrat is distributed under the terms of the GNU Lesser General Public License v3.0. See [LICENSE.txt](LICENSE.txt) for the full license.

# Contributing

Please be sure to review Karrat's [contributing guidelines](docs/contributing.md) to learn how to help the project.
