package blinky

import cafe.adriel.voyager.core.model.ScreenModel
import client.KMMClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import scanner.KMMDevice

class BlinkyViewModel(private val device: KMMDevice) : ScreenModel {

    private val _ledState = MutableStateFlow(false)
    val ledState = _ledState.asStateFlow()

    private val _buttonState = MutableStateFlow(false)
    val buttonState = _buttonState.asStateFlow()

    init {

    }
}
