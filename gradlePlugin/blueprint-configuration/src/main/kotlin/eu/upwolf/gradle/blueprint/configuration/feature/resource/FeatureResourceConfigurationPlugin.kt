/*
 * Copyright (c) 2021 Wolf-Martell Montwé. All rights reserved.
 */

package eu.upwolf.gradle.blueprint.configuration.feature.resource

import eu.upwolf.gradle.blueprint.configuration.androidLibrary
import eu.upwolf.gradle.blueprint.configuration.fixAndroidSourceSets
import eu.upwolf.gradle.blueprint.configuration.kotlin
import eu.upwolf.gradle.blueprint.configuration.setupKotlinCompatibility
import eu.upwolf.gradle.blueprint.dependency.DependencyHelper
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import java.io.File

@Suppress("UnstableApiUsage")
class FeatureResourceConfigurationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        target.pluginManager.apply("eu.upwolf.gradle.blueprint.configuration.android.library")
        target.pluginManager.apply("dev.icerock.mobile.multiplatform-resources")

        setupTargets(target)

        target.setupKotlinCompatibility()
    }

    private fun setupTargets(project: Project) {
        setupCommonTarget(project)
        setupAndroidTarget(project)
        setupDesktopTarget(project)
        setupIosTarget(project)
    }

    private fun setupCommonTarget(project: Project) {
        val libs = DependencyHelper(project)

        project.kotlin {
            sourceSets {
                maybeCreate("commonMain").dependencies {
                    api(libs.moko.resources)
                }

                maybeCreate("commonTest").dependencies {
                    implementation(libs.test.kotlin.core)
                    implementation(libs.test.kotlin.annotations)
                }
            }
        }
    }

    private fun setupAndroidTarget(project: Project) {
        val libs = DependencyHelper(project)

        project.kotlin {
            android {
                publishLibraryVariants("release")
            }

            sourceSets {
                val androidMain = maybeCreate("androidMain")
                androidMain.dependencies {
                    // nothing to add
                }
                val androidTest = maybeCreate("androidTest")
                androidTest.dependencies {
                    implementation(libs.test.kotlin.junit)
                    implementation(libs.test.junit)
                }
                fixAndroidSourceSets(androidTest)
            }
        }

        project.androidLibrary {
            sourceSets {
                getByName("main") {
                    manifest.srcFile("src/androidMain/AndroidManifest.xml")
                    kotlin.setSrcDirs(
                        setOf(
                            "src/androidMain/kotlin",
                            // FIX for https://github.com/icerockdev/moko-resources/issues/384
                            // https://github.com/icerockdev/moko-resources/issues/353
                            File(project.buildDir, "generated/moko/androidMain/src"),
                        )
                    )
                    res.setSrcDirs(
                        setOf(
                            "src/androidMain/res",
                            "src/commonMain/resources",
                            // FIX for https://github.com/icerockdev/moko-resources/issues/384
                            // https://github.com/icerockdev/moko-resources/issues/353
                            File(project.buildDir, "generated/moko/androidMain/assets"),
                        )
                    )
                    assets.setSrcDirs(
                        setOf(
                            "src/androidMain/assets",
                            "src/commonMain/assets",
                            // FIX for https://github.com/icerockdev/moko-resources/issues/384
                            // https://github.com/icerockdev/moko-resources/issues/353
                            File(project.buildDir, "generated/moko/androidMain/assets"),
                        )
                    )
                }
            }
        }
    }

    private fun setupDesktopTarget(project: Project) {
        project.kotlin {
            jvm("desktop") {
                compilations.all {
                    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
                }
            }

            sourceSets {
                maybeCreate("desktopMain").dependencies {
                    // nothing to add
                }
                maybeCreate("desktopTest").dependencies {
                    // nothing to add
                }
            }
        }
    }

    private fun setupIosTarget(project: Project) {
        project.kotlin {
            ios {
                // binaries {
                //     val projectName = project.name.capitalize(Locale.ENGLISH)
                //     val parentName = project.parent!!.name.capitalize(Locale.ENGLISH)
                //     framework("Feature$parentName$projectName")
                // }
            }

            sourceSets {
                maybeCreate("iosMain").dependencies {
                    // Nothing to add
                }
                maybeCreate("iosTest").dependencies {
                    // Nothing to add
                }
            }
        }
    }
}
