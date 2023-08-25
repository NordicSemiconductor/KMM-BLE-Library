package client

import com.benasher44.uuid.Uuid
import no.nordicsemi.android.kotlin.ble.client.main.service.ClientBleGattServices

actual class KMMServices(private val value: ClientBleGattServices) {

    actual fun findService(uuid: Uuid): KMMService? {
        return value.findService(uuid)?.let { KMMService(it) }
    }
}
