package server

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow

expect class KMMBleServerCharacteristic {
    val uuid: Uuid
    val properties: List<KMMCharacteristicProperty>
    val permissions: List<KMMBlePermission>
    val descriptors: List<KMMBleServerDescriptor>
    val value: Flow<ByteArray>
    suspend fun setValue(value: ByteArray)
}
