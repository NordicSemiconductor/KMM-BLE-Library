package server

import com.benasher44.uuid.Uuid

data class KMMBleServerService (
    val uuid: Uuid,
    val properties: List<KMMCharacteristicProperty>,
    val characteristics: List<KMMBleServerCharacteristic>
)
