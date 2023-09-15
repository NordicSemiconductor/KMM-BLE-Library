package server

import com.benasher44.uuid.Uuid
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattServices

actual data class KMMBleServerProfile(private val native: ServerBleGattServices) {

    actual val services: List<KMMBleServerService> = native.services.map {
        KMMBleServerService(it)
    }

    actual fun findService(uuid: Uuid): KMMBleServerService? {
        return services.find { it.uuid == uuid }
    }

    actual fun copyWithNewService(service: KMMBleServerService): KMMBleServerProfile {
        return copy()
    }
}
