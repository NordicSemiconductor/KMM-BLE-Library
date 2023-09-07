package server

import com.benasher44.uuid.Uuid

data class KMMBleServerProfile (
    val services: List<KMMBleServerService>
) {

    fun findService(uuid: Uuid): KMMBleServerService? {
        return services.firstOrNull { it.uuid == uuid }
    }

    fun copyWithNewService(service: KMMBleServerService): KMMBleServerProfile {
        return copy(services = services + service)
    }
}
