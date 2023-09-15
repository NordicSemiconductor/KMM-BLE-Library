package server

import com.benasher44.uuid.Uuid

expect class KMMBleServerProfile {
    val services: List<KMMBleServerService>
    fun findService(uuid: Uuid): KMMBleServerService?
    fun copyWithNewService(service: KMMBleServerService): KMMBleServerProfile
}
