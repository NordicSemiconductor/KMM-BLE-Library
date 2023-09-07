package server

import com.benasher44.uuid.Uuid

data class KMMBleServerServiceConfig (
    val uuid: Uuid,
    val characteristics: List<KMMBleServerCharacteristicConfig>
)
