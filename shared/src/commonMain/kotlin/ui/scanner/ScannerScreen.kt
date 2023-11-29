package ui.scanner

import NordicAppBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import consts.StringConst
import org.jetbrains.compose.resources.ExperimentalResourceApi

class ScannerScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { ScannerViewModel() }
        val state = viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                NordicAppBar(StringConst.SCANNER_SCREEN, onNavigationButtonClick = {
                    navigator.pop()
                })
            }
        ) {
            Box(Modifier.padding(it)) {
                ScannerView(state.value)
            }
        }
    }
}
