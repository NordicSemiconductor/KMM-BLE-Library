package server

import client.toUuid
import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBMutableCharacteristic
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBService

actual class KMMBleServerService(
    val service: CBService,
    private val manager: CBPeripheralManager,
    private val notificationsRecords: NotificationsRecords,
) {

    actual val uuid: Uuid = service.UUID.toUuid()

    actual val characteristics: List<KMMBleServerCharacteristic> = service.characteristics
        ?.map { it as CBMutableCharacteristic }
        ?.map { KMMBleServerCharacteristic(it, manager, notificationsRecords) }
        ?: emptyList()

    actual fun findCharacteristic(uuid: Uuid): KMMBleServerCharacteristic? {
        return characteristics.find { it.uuid == uuid }
    }

    fun onEvent(event: ServerRequest) {
        characteristics.forEach { it.onEvent(event) }
    }
}
