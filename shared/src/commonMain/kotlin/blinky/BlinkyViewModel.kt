package blinky

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import client.KMMCharacteristic
import client.KMMClient
import com.benasher44.uuid.uuidFrom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import scanner.KMMDevice

private val BLINKY_SERVICE_UUID = uuidFrom("00001523-1212-efde-1523-785feabcd123")
private val BLINKY_BUTTON_CHARACTERISTIC_UUID = uuidFrom("00001524-1212-efde-1523-785feabcd123")
private val BLINKY_LED_CHARACTERISTIC_UUID = uuidFrom("00001525-1212-efde-1523-785feabcd123")

class BlinkyViewModel(
    private val device: KMMDevice
) : ScreenModel, KoinComponent {

    private val client: KMMClient by inject()

    private val _state = MutableStateFlow(BlinkyViewState())
    val state = _state.asStateFlow()

    private lateinit var ledCharacteristic: KMMCharacteristic

    init {
        coroutineScope.launch {
            client.connect(device)

            val services = client.discoverServices()

            val service = services.findService(BLINKY_SERVICE_UUID)!!

            ledCharacteristic = service.findCharacteristic(BLINKY_LED_CHARACTERISTIC_UUID)!!
            val buttonCharacteristic = service.findCharacteristic(BLINKY_BUTTON_CHARACTERISTIC_UUID)!!

            buttonCharacteristic.getNotifications()
                .onEach { _state.value = _state.value.copy(isButtonPressed = BlinkyButtonParser.isButtonPressed(it))  }
                .launchIn(coroutineScope)
        }
    }

    fun turnLed() {
        coroutineScope.launch {
            if (state.value.isLedOn) {
                _state.value = _state.value.copy(isLedOn = false)
                ledCharacteristic.write(byteArrayOf(0x00))
            } else {
                _state.value = _state.value.copy(isLedOn = true)
                ledCharacteristic.write(byteArrayOf(0x01))
            }
        }
    }

    override fun onDispose() {
        coroutineScope.launch {
            client.disconnect()
        }
        super.onDispose()
    }
}
