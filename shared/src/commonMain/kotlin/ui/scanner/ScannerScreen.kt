package ui.scanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

class ScannerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { ScannerViewModel() }
        val state = viewModel.state.collectAsState()

        ScannerView(state.value)
    }
}
