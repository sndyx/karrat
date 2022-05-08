/*
 * Copyright © Karrat - 2022.
 */

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation(project(":server"))
    implementation(project(":sided"))
}