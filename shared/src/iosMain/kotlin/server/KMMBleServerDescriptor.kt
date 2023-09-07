package server

import client.toNSData
import client.toUuid
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.CoreBluetooth.CBDescriptor
import platform.Foundation.setValue

actual class KMMBleServerDescriptor(
    private val descriptor: CBDescriptor
) {

    actual val uuid: Uuid = descriptor.UUID.toUuid()

    actual val permissions: List<KMMBlePermission> = emptyList()

    private val _value = MutableStateFlow(byteArrayOf())
    actual val value: Flow<ByteArray> = _value

    actual suspend fun setValue(value: ByteArray) {
        descriptor.setValue(value.toNSData(), forKeyPath = "")
    }
}
