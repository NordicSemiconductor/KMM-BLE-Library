import androidx.compose.ui.window.ComposeUIViewController
import platform.CoreBluetooth.CBCentralManager
import scanner.KMMScanner

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App(ScannerViewModel(KMMScanner())) }
