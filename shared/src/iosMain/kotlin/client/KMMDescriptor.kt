package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class KMMDescriptor(
    private val peripheral: CBPeripheral,
    private val native: CBDescriptor,
) {

    val uuid: Uuid = native.UUID.toUuid()

    private var onDescriptorWrite: ((OnGattDescriptorWrite) -> Unit)? = null
    private var onDescriptorRead: ((OnGattDescriptorRead) -> Unit)? = null

    internal fun onEvent(event: DescriptorEvent) {
        when (event) {
            is OnGattDescriptorRead -> onDescriptorRead?.invoke(event)
            is OnGattDescriptorWrite -> onDescriptorWrite?.invoke(event)
        }
    }

    actual suspend fun write(value: ByteArray) {
        return suspendCoroutine { continuation ->
            onDescriptorWrite = {
                onDescriptorWrite = null
                continuation.resume(Unit)
            }
            peripheral.writeValue(value.toNSData(), native)
        }
    }

    actual suspend fun read(): ByteArray {
        return suspendCoroutine { continuation ->
            onDescriptorRead = {
                onDescriptorRead = null
                continuation.resume(it.data.toByteArray())
            }
            peripheral.readValueForDescriptor(native)
        }
    }
}
