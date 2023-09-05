package ui.server

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class ServerViewModel : ScreenModel, KoinComponent {

    private val _state = MutableStateFlow(ServerState())
    val state = _state.asStateFlow()

    fun advertise() {

    }

    fun stopAdvertise() {

    }

    fun onButtonPressedChanged(isButtonPressed: Boolean) {

    }
}
