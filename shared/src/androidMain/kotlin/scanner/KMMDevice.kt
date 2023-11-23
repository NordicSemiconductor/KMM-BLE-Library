package scanner

import no.nordicsemi.android.kotlin.ble.core.BleDevice

actual class KMMDevice(internal val device: BleDevice) {

    actual val name: String
        get() = device.name ?: ""

    actual val address: String
        get() = device.address

}
