package advertisement

import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerDelegateProtocol
import platform.darwin.NSObject

actual class KMMBleAdvertiser {

    actual suspend  fun advertise(settings: KMMAdvertisementSettings) {
        val manager = CBPeripheralManager(object : NSObject(), CBPeripheralManagerDelegateProtocol {
            override fun peripheralManagerDidUpdateState(peripheral: CBPeripheralManager) {

            }

        }, null)
    }
}
