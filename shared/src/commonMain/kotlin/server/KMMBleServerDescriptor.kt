package server

import com.benasher44.uuid.Uuid

data class KMMBleServerDescriptor (
    val uuid: Uuid,
    val permissions: List<KMMBlePermission>
)
