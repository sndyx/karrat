Docs / Getting Started / [Creating A Project](creating-a-project.md)

---

# Creating a Project

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

Inside of the the `build.gradle.kts` file, you must add the Karrat server
 artifact, located in the Maven Central Repository.

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.karrat:karrat-server-plugin:1.17.1")
}
```

---

Next: [Plugin Declaration](basic-plugin.md)