package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBService

actual class KMMService(private val service: CBService) {

    actual val uuid: Uuid = service.UUID.toUuid()

    actual fun findCharacteristic(uuid: Uuid): KMMCharacteristic? {
        return service.characteristics
            ?.mapNotNull { it as CBCharacteristic }
            ?.firstOrNull { it.UUID == uuid.toCBUUID() }
            ?.let { KMMCharacteristic(it) }
    }
}
