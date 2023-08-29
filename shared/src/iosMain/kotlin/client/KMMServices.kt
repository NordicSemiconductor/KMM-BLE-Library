package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService

actual class KMMServices(
    private val peripheral: CBPeripheral,
    private val native: List<CBService>,
) {

    private val services = native.map { KMMService(peripheral, it) }

    actual fun findService(uuid: Uuid): KMMService? {
        return services.firstOrNull { it.uuid == uuid }
    }

    internal fun onEvent(event: IOSGattEvent) {
        services.forEach { it.onEvent(event) }
    }
}
