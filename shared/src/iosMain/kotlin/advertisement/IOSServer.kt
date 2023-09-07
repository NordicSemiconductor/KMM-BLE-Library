package advertisement

import client.KMMCharacteristic
import client.toCBUUID
import client.toUuid
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import platform.CoreBluetooth.CBATTErrorSuccess
import platform.CoreBluetooth.CBATTRequest
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBAttributePermissions
import platform.CoreBluetooth.CBAttributePermissionsReadable
import platform.CoreBluetooth.CBAttributePermissionsWriteable
import platform.CoreBluetooth.CBCentral
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicProperties
import platform.CoreBluetooth.CBCharacteristicPropertyIndicate
import platform.CoreBluetooth.CBCharacteristicPropertyNotify
import platform.CoreBluetooth.CBCharacteristicPropertyRead
import platform.CoreBluetooth.CBCharacteristicPropertyWrite
import platform.CoreBluetooth.CBMutableCharacteristic
import platform.CoreBluetooth.CBMutableDescriptor
import platform.CoreBluetooth.CBMutableService
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheralManagerState
import platform.CoreBluetooth.CBPeripheralManagerStateUnknown
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.darwin.NSObject
import scanner.CentralDevice
import scanner.KMMDevice
import server.KMMBlePermission
import server.KMMBleServerProfile
import server.KMMBleServerService
import server.KMMBleServerServiceConfig
import server.KMMCharacteristicProperty

@Suppress("CONFLICTING_OVERLOADS")
class IOSServer : NSObject(), CBPeripheralManagerDelegateProtocol {
    private val manager = CBPeripheralManager(this, null)

    private val _bleState = MutableStateFlow(CBPeripheralManagerStateUnknown)
    val bleState: StateFlow<CBPeripheralManagerState> = _bleState.asStateFlow()

    private val _connections = MutableStateFlow<Map<CBCentral, KMMBleServerProfile>>(emptyMap())
    val connections: Flow<Map<KMMDevice, KMMBleServerProfile>> = _connections.map {
        it.mapKeys { KMMDevice(CentralDevice(it.key)) }
    }

    private val profile: KMMBleServerProfile = KMMBleServerProfile(emptyList())

    override fun peripheralManagerDidUpdateState(peripheral: CBPeripheralManager) {
        _bleState.value = peripheral.state
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didReceiveReadRequest: CBATTRequest
    ) {
        didReceiveReadRequest.value
        manager.respondToRequest(didReceiveReadRequest, CBATTErrorSuccess)
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didReceiveWriteRequests: List<*>
    ) {
        val requests = didReceiveWriteRequests.map { it as CBATTRequest }
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        central: CBCentral,
        didSubscribeToCharacteristic: CBCharacteristic
    ) {

    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        central: CBCentral,
        didUnsubscribeFromCharacteristic: CBCharacteristic
    ) {

    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didAddService: CBService,
        error: NSError?
    ) {
        val characteristics = didAddService.characteristics
            ?.map { it as CBCharacteristic } ?: emptyList()

        val kmmCharacteristics = characteristics.map { KMMCharacteristic }
        profile.copyWithNewService(KMMBleServerService(didAddService.UUID.toUuid()))
    }

    private fun KMMBleServerService(uuid: Uuid): KMMBleServerService {

    }

    suspend fun advertise(settings: KMMAdvertisementSettings) {
        bleState.first { it == CBCentralManagerStatePoweredOn }
        val map: Map<Any?, Any> = mapOf(
            CBAdvertisementDataLocalNameKey to settings.name,
            CBAdvertisementDataServiceUUIDsKey to arrayOf(settings.uuid.toCBUUID())
        )
        manager.startAdvertising(map)
    }

    fun startServer(services: List<KMMBleServerServiceConfig>) {
        val iosServices = services.map {
            val characteristics = it.characteristics.map {
                val descriptors = it.descriptors.map {
                    CBMutableDescriptor(it.uuid.toCBUUID(), null)
                }
                CBMutableCharacteristic(
                    it.uuid.toCBUUID(),
                    it.properties.toDomain(),
                    null,
                    it.permissions.toDomain()
                ).also {
                    it.setDescriptors(descriptors)
                }
            }

            CBMutableService(it.uuid.toCBUUID(), false).also {
                it.setCharacteristics(characteristics)
            }
        }

        iosServices.forEach {
            manager.addService(it)
        }
    }

    suspend fun stop() {
        manager.stopAdvertising()
    }

    private fun getProfile(central: CBCentral): KMMBleServerProfile {
        _connections.value.getOrElse(central) {
            KMMDevice(CentralDevice(central)).also {
                _connections.value = _connections.value + (central to profile.copy())
            }
        }
    }

    private fun List<KMMBlePermission>.toDomain(): CBAttributePermissions {
        return this.map {
            when (it) {
                KMMBlePermission.READ -> CBAttributePermissionsReadable
                KMMBlePermission.WRITE -> CBAttributePermissionsWriteable
            }
        }.reduce { acc, permission -> acc and permission }
    }

    private fun List<KMMCharacteristicProperty>.toDomain(): CBCharacteristicProperties {
        return this.map {
            when (it) {
                KMMCharacteristicProperty.READ -> CBCharacteristicPropertyRead
                KMMCharacteristicProperty.WRITE -> CBCharacteristicPropertyWrite
                KMMCharacteristicProperty.NOTIFY -> CBCharacteristicPropertyNotify
                KMMCharacteristicProperty.INDICATE -> CBCharacteristicPropertyIndicate
            }
        }.reduce { acc, permission -> acc and permission }
    }
}
