package blinky

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import scanner.KMMDevice

class BlinkyScreen(private val device: KMMDevice) : Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { BlinkyViewModel(device) }
        val state = viewModel.state.collectAsState()

        BlinkyView(state.value.isLedOn, state.value.isButtonPressed, { viewModel.turnLed() })
    }
}
