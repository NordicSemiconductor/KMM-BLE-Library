package client

import android.annotation.SuppressLint
import no.nordicsemi.android.common.core.DataByteArray
import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattDescriptor

@SuppressLint("MissingPermission")
actual class KMMDescriptor(private val descriptor: ClientBleGattDescriptor) {

    actual suspend fun write(value: ByteArray) {
        descriptor.write(DataByteArray(value))
    }

    actual suspend fun read(): ByteArray {
        return descriptor.read().value
    }
}
