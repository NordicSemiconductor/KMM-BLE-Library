package server

import com.benasher44.uuid.Uuid

data class KMMBleServerService (
    val uuid: Uuid,
    val characteristics: List<KMMBleServerCharacteristic>
) {

    fun findCharacteristic(uuid: Uuid): KMMBleServerCharacteristic? {
        return characteristics.firstOrNull { it.uuid == uuid }
    }
}
