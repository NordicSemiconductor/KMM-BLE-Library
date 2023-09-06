package advertisement

import client.toCBUUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheralManagerState
import platform.CoreBluetooth.CBPeripheralManagerStateUnknown
import platform.darwin.NSObject

class IOSServer : NSObject(), CBPeripheralManagerDelegateProtocol {
    private val manager = CBPeripheralManager(this, null)

    private val _bleState = MutableStateFlow(CBPeripheralManagerStateUnknown)
    val bleState: StateFlow<CBPeripheralManagerState> = _bleState.asStateFlow()

    override fun peripheralManagerDidUpdateState(peripheral: CBPeripheralManager) {
        _bleState.value = peripheral.state
    }

    suspend fun advertise(settings: KMMAdvertisementSettings) {
        bleState.first { it == CBCentralManagerStatePoweredOn }
        val map: Map<Any?, Any> = mapOf(
            CBAdvertisementDataLocalNameKey to settings.name,
            CBAdvertisementDataServiceUUIDsKey to arrayOf(settings.uuid.toCBUUID())
        )
        manager.startAdvertising(map)
    }

    suspend fun stop() {
        manager.stopAdvertising()
    }
}
