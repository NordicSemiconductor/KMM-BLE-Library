package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

actual class KMMService(private val peripheral: CBPeripheral, private val native: CBService) {

    private val characteristics = native.characteristics
        ?.map { it as CBCharacteristic }
        ?.map { KMMCharacteristic(peripheral, it) }
        ?: emptyList()

    actual val uuid: Uuid = native.UUID.toUuid()

    actual fun findCharacteristic(uuid: Uuid): KMMCharacteristic? {
        return characteristics.firstOrNull { it.uuid == uuid }
    }

    internal fun onEvent(event: IOSGattEvent) {
        (event as? CharacteristicEvent)?.let {
            characteristics.onEach { it.onEvent(event) }
        }
    }
}
