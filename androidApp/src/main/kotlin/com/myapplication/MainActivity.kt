package com.myapplication

import MainView
import ScannerViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import no.nordicsemi.android.common.theme.NordicActivity
import scanner.KMMScanner

@AndroidEntryPoint
class MainActivity : NordicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scannerViewModel = ScannerViewModel(KMMScanner(this))

        setContent {
            MainView(scannerViewModel)
        }
    }
}