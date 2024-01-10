/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package advertisement

import client.toCBUUID
import client.toUuid
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
import scanner.IoTDevice
import server.GattPermission
import server.ServerProfile
import server.BleServerServiceConfig
import server.GattProperty
import server.NotificationsRecords
import server.ReadRequest
import server.WriteRequest

private const val TAG = "BLE-TAG"

@Suppress("CONFLICTING_OVERLOADS")
class IOSServer(
    private val notificationsRecords: NotificationsRecords,
) : NSObject(), CBPeripheralManagerDelegateProtocol {
    private val manager = CBPeripheralManager(this, null)

    private val _bleState = MutableStateFlow(CBPeripheralManagerStateUnknown)
    val bleState: StateFlow<CBPeripheralManagerState> = _bleState.asStateFlow()

    private val _connections = MutableStateFlow<Map<CBCentral, ServerProfile>>(emptyMap())
    val connections: Flow<Map<IoTDevice, ServerProfile>> = _connections.map {
        it.mapKeys { IoTDevice(CentralDevice(it.key)) }
    }

    private var services = listOf<CBService>()

    override fun peripheralManagerDidUpdateState(peripheral: CBPeripheralManager) {
        _bleState.value = peripheral.state
    }

    override fun peripheralManagerDidStartAdvertising(
        peripheral: CBPeripheralManager,
        error: NSError?
    ) {

    }

    override fun peripheralManagerIsReadyToUpdateSubscribers(peripheral: CBPeripheralManager) {
        Napier.i("Update subscribers", tag = TAG)
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didReceiveReadRequest: CBATTRequest
    ) {
        Napier.i("Receive read request", tag = TAG)
        val central = didReceiveReadRequest.central
        val profile = getProfile(central)

        profile.onEvent(ReadRequest(didReceiveReadRequest))
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didReceiveWriteRequests: List<*>
    ) {
        Napier.i("Receive write request", tag = TAG)
        try {

            val requests = didReceiveWriteRequests.map { it as CBATTRequest }
            Napier.i("Requests: $requests", tag = TAG)
            val central = requests.first().central
            Napier.i("Central: $central", tag = TAG)
            val profile = getProfile(central)
            Napier.i("Profile: $profile", tag = TAG)
            profile.onEvent(WriteRequest(requests))
        } catch (t: Throwable) {
            Napier.i("Receive write request", tag = TAG, throwable = t)
        }

    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        central: CBCentral,
        didSubscribeToCharacteristic: CBCharacteristic
    ) {
        Napier.i("Subscribe to characteristic", tag = TAG)
        notificationsRecords.addCentral(didSubscribeToCharacteristic.UUID.toUuid(), central)
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        central: CBCentral,
        didUnsubscribeFromCharacteristic: CBCharacteristic
    ) {
        Napier.i("Unsubscribe from characteristic", tag = TAG)
        notificationsRecords.removeCentral(didUnsubscribeFromCharacteristic.UUID.toUuid(), central)
    }

    override fun peripheralManager(
        peripheral: CBPeripheralManager,
        didAddService: CBService,
        error: NSError?
    ) {
        Napier.i("Add service", tag = TAG)
        services = services + didAddService
    }

    suspend fun advertise(settings: AdvertisementSettings) {
        bleState.first { it == CBCentralManagerStatePoweredOn }
        val map: Map<Any?, Any> = mapOf(
            CBAdvertisementDataLocalNameKey to settings.name,
            CBAdvertisementDataServiceUUIDsKey to listOf(settings.uuid.toCBUUID()) //https://kotlinlang.org/docs/native-objc-interop.html#mappings
        )
        manager.startAdvertising(map)
    }

    suspend fun startServer(services: List<BleServerServiceConfig>) {
        bleState.first { it == CBCentralManagerStatePoweredOn }
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

            CBMutableService(it.uuid.toCBUUID(), true).also {
                it.setCharacteristics(characteristics)
            }
        }

        iosServices.forEach {
            manager.addService(it)
        }
    }

    suspend fun stopAdvertising() {
        manager.stopAdvertising()
    }

    private fun getProfile(central: CBCentral): ServerProfile {
        return _connections.value.getOrElse(central) {
            val profile = ServerProfile(services, manager, notificationsRecords)
            _connections.value = _connections.value + (central to profile)
            profile
        }
    }

    private fun List<GattPermission>.toDomain(): CBAttributePermissions {
        return this.map {
            when (it) {
                GattPermission.READ -> CBAttributePermissionsReadable
                GattPermission.WRITE -> CBAttributePermissionsWriteable
            }
        }.reduce { acc, permission -> acc or permission }
    }

    private fun List<GattProperty>.toDomain(): CBCharacteristicProperties {
        return this.map {
            when (it) {
                GattProperty.READ -> CBCharacteristicPropertyRead
                GattProperty.WRITE -> CBCharacteristicPropertyWrite
                GattProperty.NOTIFY -> CBCharacteristicPropertyNotify
                GattProperty.INDICATE -> CBCharacteristicPropertyIndicate
            }
        }.reduce { acc, permission -> acc or permission }
    }
}
