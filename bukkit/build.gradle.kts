/*
 * Copyright © Karrat - 2022.
 */

plugins {
    kotlin("jvm")
}

group = "org.karrat.bukkit"
version = "1.17.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":server"))
    implementation("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().forEach {
    it.kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
}