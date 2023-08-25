package client

import com.benasher44.uuid.Uuid
import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattService

actual class KMMService(private val service: ClientBleGattService) {

    actual val uuid: Uuid = service.uuid

    actual fun findCharacteristic(uuid: Uuid): KMMCharacteristic? {
        return service.findCharacteristic(uuid)?.let { KMMCharacteristic(it) }
    }
}
