package server

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.common.core.DataByteArray
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattDescriptor

actual data class KMMBleServerDescriptor(
    val native: ServerBleGattDescriptor
) {
    actual val uuid: Uuid = native.uuid

    actual val permissions: List<KMMBlePermission> = emptyList() //FIXME

    actual val value: Flow<ByteArray> = native.value.map { it.value }

    actual suspend fun setValue(value: ByteArray) {
        native.setValue(DataByteArray(value))
    }
}
