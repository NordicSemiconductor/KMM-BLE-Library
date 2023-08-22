package scanner

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSError
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
                peripheral: CBPeripheral,
                advertisementData: Map<Any?, *>,
                RSSI: NSNumber
            ) {
                val kmmDevice = KMMDevice(peripheral)
                trySend(kmmDevice)

                val a= central.connectPeripheral(peripheral, options = null)


            }

            override fun centralManager(
                central: CBCentralManager,
                peripheral: CBPeripheral
            ) {
                super.centralManager(central, peripheral)
                peripheral.discoverServices(null)
            }

            override fun centralManager(
                central: CBCentralManager,
                peripheral: CBPeripheral,
                error: NSError?
            ) {
                super.centralManager(central, peripheral, error)

                peripheral.delegate = object : NSObject(), CBPeripheralDelegateProtocol {

                    override fun peripheral(
                        peripheral: CBPeripheral,
                        services: NSError?
                    ) {
                        super.peripheral(peripheral, services)
                    }

                    override fun peripheral(peripheral: CBPeripheral, didModifyServices: List<*>) {
                        super.peripheral(peripheral, didModifyServices)

                    }

                    override fun peripheral(
                        peripheral: CBPeripheral,
                        service: CBService,
                        error: NSError?
                    ) {
                        super.peripheral(peripheral, service, error)


                        val heartRateMeasurementCharacteristicCBUUID = CBUUID.UUIDWithString("2A37")
                        val bodySensorLocationCharacteristicCBUUID = CBUUID.UUIDWithString("2A38")

                        var heartRateMeasurement : CBCharacteristic? = null
                        var bodySensorMeasurement : CBCharacteristic? = null
                        service.characteristics?.map {
                            it as CBCharacteristic
                        }?.forEach {
                            if (it.UUID == heartRateMeasurementCharacteristicCBUUID) {
                                heartRateMeasurement = it
                            } else if (it.UUID == bodySensorLocationCharacteristicCBUUID) {
                                bodySensorMeasurement = it
                            }
                        }

                        peripheral.readValueForCharacteristic(heartRateMeasurement!!)
                    }

                    override fun peripheral(
                        peripheral: CBPeripheral,
                        characterist: CBCharacteristic,
                        error: NSError?
                    ) {
                        super.peripheral(peripheral, characterist, error)
                        val freshValue = characterist.value
                    }
                }
                peripheral.discoverServices(null)
            }

        }, queue = null)

        awaitClose {
            cbCentralManager.stopScan()
        }
    }
}
