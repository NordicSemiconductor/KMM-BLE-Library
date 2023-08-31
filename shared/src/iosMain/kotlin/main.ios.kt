import androidx.compose.ui.window.ComposeUIViewController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import scanner.KMMScanner

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App(ScannerViewModel(KMMScanner())) }
    .also { Napier.base(DebugAntilog()) }
