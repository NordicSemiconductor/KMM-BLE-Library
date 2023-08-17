package com.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import scanner.CommonModule
import scanner.ScannerModule

@HiltAndroidApp
class MainApplication : Application() {

    init {
        startKoin {
            androidContext(this@MainApplication)
            modules(ScannerModule, CommonModule)
        }
        Napier.base(DebugAntilog())
    }
}
