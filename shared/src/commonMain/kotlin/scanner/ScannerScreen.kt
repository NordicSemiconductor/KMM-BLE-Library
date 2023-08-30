package scanner

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class ScannerScreen : Screen {

    @Composable
    override fun Content() {
        ScannerView()
    }
}