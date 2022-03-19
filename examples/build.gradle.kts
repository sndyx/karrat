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
    implementation(kotlin("stdlib"))
    implementation(project(":server"))
    implementation(project(":sided"))
}