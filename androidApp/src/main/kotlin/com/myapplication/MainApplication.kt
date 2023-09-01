package com.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

@HiltAndroidApp
class MainApplication : Application() {

    init {
        initKoin {
            androidContext(this@MainApplication)
        }
        Napier.base(DebugAntilog())
    }
}
