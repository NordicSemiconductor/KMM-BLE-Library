plugins {
    alias(libs.plugins.nordic.application)
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

kotlin {
    androidTarget()
    sourceSets {

        val androidMain by getting {
            dependencies {
//                configurations.get("kapt").dependencies.add(libs.hilt.compiler)
                implementation(project(":shared"))

                implementation(libs.hilt.android)

                implementation(libs.nordic.blek.scanner)

                implementation(libs.nordic.theme)
                implementation(libs.nordic.navigation)
                implementation(libs.nordic.permissions.ble)
                implementation(libs.nordic.logger)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.compose.material3)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.hilt.navigation.compose)
                implementation(libs.androidx.compose.material.iconsExtended)
            }
        }
    }
}

group = "no.nordicsemi.android.kotlin.ble"

android {
    namespace = "no.nordicsemi.android.kotlin.ble.app.mock"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    configurations["kapt"].dependencies.add(project.dependencies.create("com.google.dagger:hilt-android-compiler:2.47"))
}
