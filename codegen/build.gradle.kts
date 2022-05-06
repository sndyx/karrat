/*
 * Copyright © Karrat - 2022.
 */

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.21"
    id("application")
}

group = "org.karrat.codegen"
version = "1.17.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
}

application {
    mainClass.set("org.karrat.codegen.MainKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().forEach {
    it.kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}