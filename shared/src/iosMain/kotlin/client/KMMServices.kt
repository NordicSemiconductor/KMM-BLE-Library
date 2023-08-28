package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBService

actual class KMMServices(private val services: List<CBService>) {

    actual fun findService(uuid: Uuid): KMMService? {
        return services.firstOrNull { it.UUID == uuid.toCBUUID()}?.let { KMMService(it) }
    }
}
