package blinky

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import scanner.KMMDevice

class BlinkyScreen(private val device: KMMDevice) : Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { BlinkyViewModel(device) }
        val state = viewModel.state.collectAsState()

        BlinkyView(
            state.value.isLedOn,
            state.value.isButtonPressed,
            { viewModel.turnLed() },
            Modifier.padding(16.dp)
        )
    }
}
