package advertisement

import com.benasher44.uuid.Uuid

data class KMMBleServerService (
    val uuid: Uuid,
    val properties: List<KMMCharacteristicProperty>
)


