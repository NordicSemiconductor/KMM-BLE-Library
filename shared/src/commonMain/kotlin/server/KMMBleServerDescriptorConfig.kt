package server

import com.benasher44.uuid.Uuid

data class KMMBleServerDescriptorConfig (
    val uuid: Uuid,
    val permissions: List<KMMBlePermission>
)