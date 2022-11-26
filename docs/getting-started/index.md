[Docs](../index.md) / [Getting Started](index.md)

# Getting Started

## Creating a Project

To create your project using [Gradle](https://gradle.org), you
 must first create your root directory.

```
example-plugin
```

Next, navigate to the directory, and run initialize your gradle
 project using `gradle init --type kotlin-library`.
 
Gradle will create your project structure automatically, your
 directory should now look like this:

```
example-plugin
├── build.gradle.kts
├── gradle
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── src
    ├── main
    └── test
```

## Setup

Using the Karrat API requires Kotlin compiler 1.5.0 or higher.

### Gradle

To set up your plugin development environment, simply add the artifact to your build.gradle(.kts) file.

Kotlin DSL:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.karrat:karrat-server-plugin:1.17.1")
}
```

Groovy DSL:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation "org.karrat:karrat-server-plugin:1.17.1"
}
```

We also provide the karrat-server artifact that contains all the essential server code but does not have plugin support bundled with it.

### Maven (Not Recommended)

To set up your plugin development environment, simply add the artifact to your pom.xml file.

```xml
<dependency>
    <groupId>org.karrat</groupId>
    <artifactId>karrat-server-plugin</artifactId>
    <version>1.17.1</version>
</dependency>
```
