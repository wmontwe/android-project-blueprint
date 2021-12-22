/*
 * Copyright (c) 2021 Wolf-Martell Montwé. All rights reserved.
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    includeBuild("gradlePlugin/blueprint-dependency")
    includeBuild("gradlePlugin/blueprint-configuration")
    includeBuild("gradlePlugin/blueprint-tools")
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Gradle Plugin

// App
include(
    ":app-android",
    ":app-desktop",
    ":app-ios",
)

// Common
include(
    ":common:component",
    ":common:database",
    ":common:theme",
)

// Feature
include(
    ":feature:home",
)

rootProject.name = "MobileProjectBlueprint"
