/*
 * Copyright © Karrat - 2022.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.21"
}

group = "org.karrat.server"
version = "1.18.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(kotlin("test"))
}

tasks {
    
    test {
        useJUnitPlatform()
    }
    
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "org.karrat.RunKt"
        }
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from ({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
    
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-Xexplicit-api=strict",
                "-Xbackend-threads=0" // Multi-threaded compilation 😎
            )
        }
    }
    
}

task<Copy>("copyJar") {
    group = "server"
    dependsOn("build")
    from("build/libs/server-1.18.2.jar")
    into("build/server")
}

task("clear") {
    group = "server"
    files("build/server").forEach { it.delete() }
}

task<JavaExec>("run") {
    standardInput = System.`in`
    group = "server"
    dependsOn("copyJar")
    workingDir = file("build/server")
    classpath = files("build/server/server-1.18.2.jar")
    args = listOf("--dev-env")
}