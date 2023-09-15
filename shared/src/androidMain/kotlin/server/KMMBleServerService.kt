package server

import com.benasher44.uuid.Uuid
import no.nordicsemi.android.kotlin.ble.server.main.service.ServerBleGattService

actual data class KMMBleServerService(private val native: ServerBleGattService) {

    actual val uuid: Uuid = native.uuid

    actual val characteristics: List<KMMBleServerCharacteristic> = native.characteristics
        .map { KMMBleServerCharacteristic(it) }

    actual fun findCharacteristic(uuid: Uuid): KMMBleServerCharacteristic? {
        return characteristics.first { it.uuid == uuid }
    }
}
