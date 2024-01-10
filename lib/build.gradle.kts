import no.nordicsemi.android.buildlogic.getVersionNameFromTags

/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.nordic.library)
    alias(libs.plugins.nordic.nexus)
    kotlin("native.cocoapods")
}

group = "no.nordicsemi.kmm"
version = getVersionNameFromTags()

nordicNexusPublishing {
    POM_ARTIFACT_ID = "ble"
    POM_NAME = "Nordic Kotlin Multiplatform Library for BLE."

    POM_DESCRIPTION = "Nordic Kotlin Multiplatform Library for BLE."
    POM_URL = "https://github.com/NordicSemiconductor/KMM-BLE-Library"
    POM_SCM_URL = "https://github.com/NordicSemiconductor/KMM-BLE-Library"
    POM_SCM_CONNECTION = "scm:git@github.com:NordicPlayground/KMM-BLE-Library.git"
    POM_SCM_DEV_CONNECTION = "scm:git@github.com:NordicPlayground/KMM-BLE-Library.git"

    POM_DEVELOPER_ID = "syzi"
    POM_DEVELOPER_NAME = "Sylwester Zieliński"
    POM_DEVELOPER_EMAIL = "sylwester.zielinski@nordicsemi.no"
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    cocoapods {
        version = "0.0.0"
        summary = "Nordic Kotlin Multiplatform Library for BLE."
        homepage = "https://github.com/NordicSemiconductor/KMM-BLE-Library"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kmm.uuid)
                implementation(libs.kmm.logs)
                implementation(libs.kmm.voyager)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.activity.compose)
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
                implementation(libs.nordic.blek.scanner)
                implementation(libs.nordic.blek.client)
                implementation(libs.nordic.blek.advertiser)
                implementation(libs.nordic.blek.server)
                implementation(libs.nordic.permissions.ble)
                implementation(libs.nordic.permissions.internet)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "no.nordicsemi.kmm.ble"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

//FIXME: For some reason nordic.nexus plugin is not working for kmm project.
signing {
    sign(publishing.publications)
}

publishing {
    publications.withType(MavenPublication::class) {
        groupId = "no.nordicsemi.kmm"
        artifactId = "ble"
        version = getVersionNameFromTags()

        pom {
            name.set("Nordic Kotlin Multiplatform Library for BLE.")
            description.set("Nordic Kotlin Multiplatform Library for BLE.")
            url.set("https://github.com/NordicSemiconductor/KMM-BLE-Library")

            licenses {
                license {
                    name.set("BSD-3-Clause")
                    url.set("http://opensource.org/licenses/BSD-3-Clause")
                }
            }
            developers {
                developer {
                    id.set("mag")
                    name.set("Mobile Applications Group")
                    email.set("mag@nordicsemi.no")
                }
            }
            organization {
                name.set("Nordic Semiconductor ASA")
            }
            scm {
                connection.set("https://github.com/NordicSemiconductor/KMM-BLE-Library")
                developerConnection.set("scm:git@github.com:NordicPlayground/KMM-BLE-Library.git")
                url.set("scm:git@github.com:NordicPlayground/KMM-BLE-Library.git")
            }
        }
    }
}

afterEvaluate {
    tasks.getByName("signMavenPublicationPublication").dependsOn(tasks.getByName("publishAndroidReleasePublicationToMavenRepository"))
    tasks.getByName("signMavenPublicationPublication").dependsOn(tasks.getByName("publishAndroidReleasePublicationToMavenLocal"))
}
