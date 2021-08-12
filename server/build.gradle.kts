/*
 * Copyright © Karrat - 2021.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "org.karrat.server"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}