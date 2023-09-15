package server

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBPeripheralManager

actual data class KMMBleServerProfile(
    actual val services: List<KMMBleServerService>,
    private val manager: CBPeripheralManager,
    private val notificationsRecords: NotificationsRecords,
) {

    actual fun findService(uuid: Uuid): KMMBleServerService? {
        return services.first { it.uuid == uuid }
    }

    actual fun copyWithNewService(service: KMMBleServerService): KMMBleServerProfile {
        return copy()
    }

    fun onEvent(event: ServerRequest) {
        services.forEach { it.onEvent(event) }
    }
}
