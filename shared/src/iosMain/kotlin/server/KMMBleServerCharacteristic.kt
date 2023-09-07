package server

import client.toNSData
import client.toUuid
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.Foundation.setValue

actual class KMMBleServerCharacteristic(
    val native: CBCharacteristic
) {
    actual val uuid: Uuid = native.UUID.toUuid()

    actual val properties: List<KMMCharacteristicProperty> = native.properties.toProperties()

    actual val permissions: List<KMMBlePermission> = emptyList()

    actual val descriptors: List<KMMBleServerDescriptor> = native.descriptors
        ?.map { it as CBDescriptor }
        ?.map { KMMBleServerDescriptor(it) }
        ?: emptyList()

    private val _value = MutableStateFlow(byteArrayOf())
    actual val value: Flow<ByteArray> = _value

    actual suspend fun setValue(value: ByteArray) {
        native.setValue(value.toNSData(), forKeyPath = "")
    }
}


