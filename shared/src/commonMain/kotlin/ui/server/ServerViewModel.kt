package ui.server

import advertisement.KMMAdvertisementSettings
import advertisement.KMMBleAdvertiser
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.benasher44.uuid.uuidFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val BLINKY_SERVICE_UUID = uuidFrom("00001523-1212-efde-1523-785feabcd123")
private val BLINKY_BUTTON_CHARACTERISTIC_UUID = uuidFrom("00001524-1212-efde-1523-785feabcd123")
private val BLINKY_LED_CHARACTERISTIC_UUID = uuidFrom("00001525-1212-efde-1523-785feabcd123")

class ServerViewModel : ScreenModel, KoinComponent {

    private val advertiser: KMMBleAdvertiser by inject()

    private val _state = MutableStateFlow(ServerState())
    val state = _state.asStateFlow()

    fun advertise() {
        coroutineScope.launch {
            advertiser.advertise(KMMAdvertisementSettings(
                name = "Super Server",
                uuid = BLINKY_SERVICE_UUID
            ))
        }
    }

    fun stopAdvertise() {

    }

    fun onButtonPressedChanged(isButtonPressed: Boolean) {

    }
}
