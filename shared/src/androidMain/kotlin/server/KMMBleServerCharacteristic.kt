package server

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.common.core.DataByteArray
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattCharacteristic

actual class KMMBleServerCharacteristic(
    private val native: ServerBleGattCharacteristic
) {

    actual val uuid: Uuid = native.uuid

    actual val properties: List<KMMCharacteristicProperty> = native.properties.toDomainProperties()

    actual val permissions: List<KMMBlePermission> = native.permissions.toDomainPermissions()

    actual val descriptors: List<KMMBleServerDescriptor> = native.descriptors.map {
        KMMBleServerDescriptor(it)
    }

    actual val value: Flow<ByteArray> = native.value.map { it.value }

    actual suspend fun setValue(value: ByteArray) {
        native.setValueAndNotifyClient(DataByteArray(value))
    }
}
