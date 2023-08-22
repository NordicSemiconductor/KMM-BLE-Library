package scanner

import platform.CoreBluetooth.CBPeripheral

actual class KMMDevice(private val peripheral: CBPeripheral) {

    actual val name: String
        get() = peripheral.name ?: "Unknown"

    actual val address: String? = null
}
