/*
 * Copyright (c) 2021 Wolf-Martell Montwé. All rights reserved.
 */

package eu.upwolf.gradle.blueprint.configuration.app.desktop

import eu.upwolf.gradle.blueprint.configuration.kotlin
import eu.upwolf.gradle.blueprint.configuration.setupKotlinCompatibility
import eu.upwolf.gradle.blueprint.dependency.DependencyHelper
import eu.upwolf.gradle.blueprint.dependency.VersionHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.compose

class DesktopAppConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        target.pluginManager.apply("org.jetbrains.compose")

        target.repositories {
            mavenCentral()
            google()
            maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        }

        setupDesktopApplication(target)

        target.setupKotlinCompatibility(
            listOf(
                "-opt-in=kotlin.RequiresOptIn"
            )
        )
    }

    private fun setupDesktopApplication(project: Project) {
        val libs = DependencyHelper(project)

        project.kotlin {
            jvm("desktop") {
                compilations.all {
                    kotlinOptions.jvmTarget = "11"
                }
            }

            sourceSets {
                maybeCreate("commonMain").dependencies {
                    implementation(libs.kotlin.core)
                }

                maybeCreate("desktopMain").dependencies {
                    api(ComposePlugin.Dependencies.runtime)
                    api(ComposePlugin.Dependencies.foundation)
                    api(ComposePlugin.Dependencies.material)
                    implementation(ComposePlugin.Dependencies.desktop.currentOs)
                }
                maybeCreate("desktopTest").dependencies {
                    // nothing to add
                }
            }
        }
    }
}
