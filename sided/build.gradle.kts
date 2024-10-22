/*
 * Copyright © Karrat - 2022.
 */

plugins {
    kotlin("jvm")
}

group = "org.karrat.sided"
version = "1.17.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":server"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().forEach {
    it.kotlinOptions.freeCompilerArgs += "-Xexplicit-api=strict"
}