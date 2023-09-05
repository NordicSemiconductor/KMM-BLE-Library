package advertisement

import com.benasher44.uuid.Uuid

data class KMMBleServerCharacteristic (
    val uuid: Uuid,
    val properties: List<KMMCharacteristicProperty>,
    val permissions: List<KMMCharacteristicPermission>,
    val descriptors: List<KMMBleServerDescriptor>
)
