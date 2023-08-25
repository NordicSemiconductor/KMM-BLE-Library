package client

import com.benasher44.uuid.Uuid
import platform.CoreBluetooth.CBCharacteristic

actual class KMMCharacteristic(private val native: CBCharacteristic) {

    val uuid: Uuid = native.UUID.toUuid()
}
