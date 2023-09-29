package server

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBService

actual data class KMMBleServerProfile(
    private val nativeServices: List<CBService>,
    private val manager: CBPeripheralManager,
    private val notificationsRecords: NotificationsRecords,
) {

    actual val services: List<KMMBleServerService> = nativeServices.map {
        KMMBleServerService(it, manager, notificationsRecords)
    }

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
