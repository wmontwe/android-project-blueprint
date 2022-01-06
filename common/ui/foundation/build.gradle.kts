/*
 * Copyright (c) 2021 Wolf-Martell Montwé. All rights reserved.
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.gradleBlueprintConfigurationCommonUi)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.moko.resources)
            }
        }
        androidMain {
            dependencies {
                api(libs.androidx.appCompat)
                api(libs.androidx.core.ktx)
            }
        }
    }
}