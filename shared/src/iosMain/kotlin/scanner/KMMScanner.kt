package scanner

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.darwin.NSObject

actual class KMMScanner {

    @ExperimentalForeignApi
    actual fun scan(): Flow<KMMDevice> = callbackFlow {
        val cbCentralManager = CBCentralManager(object : NSObject(), CBCentralManagerDelegateProtocol {
            override fun centralManagerDidUpdateState(central: CBCentralManager) {

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

        cbCentralManager.scanForPeripheralsWithServices(null, null)

        awaitClose {
            cbCentralManager.stopScan()
        }
    }
}
