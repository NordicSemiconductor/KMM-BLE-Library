package scanner

import no.nordicsemi.android.kotlin.ble.core.ServerDevice

actual class KMMDevice(private val device: ServerDevice) {

    actual val name: String
        get() = device.name

    actual val address: String?
        get() = device.address

}
