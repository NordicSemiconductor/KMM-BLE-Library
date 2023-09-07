package server

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow

expect class KMMBleServerDescriptor {
    val uuid: Uuid
    val permissions: List<KMMBlePermission>
    val value: Flow<ByteArray>
    suspend fun setValue(value: ByteArray)
}
