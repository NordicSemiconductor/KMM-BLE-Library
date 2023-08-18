package com.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import scanner.ScannerSharedModule
import scanner.ScannerModule

@HiltAndroidApp
class MainApplication : Application() {

    init {
        initKoin {
            androidContext(this@MainApplication)
        }
        Napier.base(DebugAntilog())
    }
}
