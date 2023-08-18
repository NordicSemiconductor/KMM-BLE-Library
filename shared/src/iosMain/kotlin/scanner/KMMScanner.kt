package scanner

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class KMMScanner {

    actual fun scan(): Flow<KMMDevice> = callbackFlow {
        val cbCentralManager = CBCentralManager(object : NSObject(), CBCentralManagerDelegateProtocol {
            override fun centralManagerDidUpdateState(central: CBCentralManager) {
                if (central.state == CBManagerStatePoweredOn) {
                    // Turned on
                    central.scanForPeripheralsWithServices(null, null)
                }
                else {
//                    isBluetoothReady = false
                }
            }

            override fun centralManager(
                central: CBCentralManager,
                didDiscoverPeripheral: CBPeripheral,
                advertisementData: Map<Any?, *>,
                RSSI: NSNumber
            ) {
                val kmmDevice = KMMDevice(didDiscoverPeripheral.name ?: "Unknown", "00:00:00:00:00")
                trySend(kmmDevice)
            }

        }, queue = null)



        awaitClose {
            cbCentralManager.stopScan()
        }
    }
}
