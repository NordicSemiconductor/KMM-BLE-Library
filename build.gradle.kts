plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    kotlin("kapt").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    alias(libs.plugins.nordic.application.compose).apply(false)
    alias(libs.plugins.nordic.application).apply(false)
    alias(libs.plugins.nordic.hilt).apply(false)
    alias(libs.plugins.hilt).apply(false)
}
