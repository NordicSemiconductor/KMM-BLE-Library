package server

import client.toByteArray
import client.toNSData
import client.toUuid
import com.benasher44.uuid.Uuid
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.CoreBluetooth.CBATTErrorSuccess
import platform.CoreBluetooth.CBATTRequest
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBMutableCharacteristic
import platform.CoreBluetooth.CBPeripheralManager

private const val TAG = "BLE-TAG"

actual class KMMBleServerCharacteristic(
    private val native: CBMutableCharacteristic,
    private val manager: CBPeripheralManager,
    private val notificationsRecords: NotificationsRecords,
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

    fun onEvent(event: ServerRequest) {
        when (event) {
            is ReadRequest -> handleReadRequest(event.request)
            is WriteRequest -> handleWriteRequest(event.request)
        }
    }

    private fun handleReadRequest(request: CBATTRequest) {
        if (request.characteristic().UUID != native.UUID) {
            return
        }
        Napier.i("handleReadRequest", tag = TAG)
        val dataToSend = _value.value.copyOfRange(
            _value.value.size - request.offset.toInt(),
            _value.value.size
        )

        request.value = dataToSend.toNSData()
        manager.respondToRequest(request, CBATTErrorSuccess)
    }

    private fun handleWriteRequest(requests: List<CBATTRequest>) {
        Napier.i("handleWriteRequest", tag = TAG)
        requests.filter { it.characteristic().UUID == native.UUID }.forEach {
            it.value?.toByteArray()?.let {
                setValue(it)
            }
            manager.respondToRequest(it, CBATTErrorSuccess)
        }
    }

    actual fun setValue(value: ByteArray) {
        _value.value = value
        val centralsToUpdate = notificationsRecords.getCentrals(native.UUID.toUuid())

        val newValue = value.toNSData()
        native.setValue(newValue)

        manager.updateValue(newValue, native, centralsToUpdate)
    }
}
