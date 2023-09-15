package server

import com.benasher44.uuid.Uuid

expect class KMMBleServerService {
    val uuid: Uuid
    val characteristics: List<KMMBleServerCharacteristic>
    fun findCharacteristic(uuid: Uuid): KMMBleServerCharacteristic?
}
