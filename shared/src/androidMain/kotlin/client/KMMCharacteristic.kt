package client

import android.annotation.SuppressLint
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.nordicsemi.android.common.core.DataByteArray
import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattCharacteristic
import no.nordicsemi.android.kotlin.ble.core.data.BleWriteType

@SuppressLint("MissingPermission")
actual class KMMCharacteristic(private val characteristic: ClientBleGattCharacteristic) {

    actual fun findDescriptor(uuid: Uuid): KMMDescriptor? {
        return characteristic.findDescriptor(uuid)?.let { KMMDescriptor(it) }
    }

    actual suspend fun getNotifications(): Flow<ByteArray> {
        return characteristic.getNotifications().map { it.value }
    }

    actual suspend fun write(value: ByteArray, writeType: KMMWriteType) {
        val kmmWriteType = when (writeType) {
            KMMWriteType.DEFAULT -> BleWriteType.DEFAULT
            KMMWriteType.NO_RESPONSE -> BleWriteType.NO_RESPONSE
        }
        characteristic.write(DataByteArray(value), kmmWriteType)
    }

    actual suspend fun read(): ByteArray {
        return characteristic.read().value
    }
}
