/*
 * Copyright © Karrat - 2023.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.7.0"
}

group = "org.karrat.server"
version = "1.19.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation(kotlin("test"))
}

tasks {
    
    test {
        useJUnitPlatform()
    }
    
    jar {
        archiveBaseName.set("Karrat")
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
                "-Xbackend-threads=0", // Multi-threaded compilation 😎
                "-Xcontext-receivers"
            )
        }
    }
    
}

task<Copy>("copyJar") {
    group = "server"
    dependsOn("build")
    from("build/libs/Karrat-1.19.2.jar")
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
    classpath = files("build/server/Karrat-1.19.2.jar")
    args = listOf("--dev-env")
}