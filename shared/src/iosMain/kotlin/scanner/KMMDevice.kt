package scanner

import platform.CoreBluetooth.CBPeripheral

actual data class KMMDevice(internal val peripheral: CBPeripheral) {

    actual val name: String
        get() = peripheral.name ?: "Unknown"

    actual val address: String = "00:00:00:00:${counter++}"

    companion object {

        var counter: Int = 0
    }
}
