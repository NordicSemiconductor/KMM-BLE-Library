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

package client

import com.benasher44.uuid.uuidFrom
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBManagerState
import platform.CoreBluetooth.CBManagerStateUnknown
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import scanner.KMMDevice
import scanner.PeripheralDevice
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "BLE-TAG"
private val BLINKY_SERVICE_UUID = uuidFrom("00001523-1212-efde-1523-785feabcd123")

@Suppress("CONFLICTING_OVERLOADS")
class IOSClient : NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {

    private lateinit var peripheral: CBPeripheral

    private val manager = CBCentralManager(this, null)

    private var onDeviceConnected: ((DeviceConnectionState) -> Unit)? = null
    private var onDeviceDisconnected: (() -> Unit)? = null
    private var onServicesDiscovered: ((OperationStatus) -> Unit)? = null

    private var services: KMMServices? = null

    private val _scannedDevices = MutableStateFlow<List<KMMDevice>>(emptyList())
    val scannedDevices = _scannedDevices.asStateFlow()

    private val _bleState = MutableStateFlow(CBManagerStateUnknown)
    val bleState: StateFlow<CBManagerState> = _bleState.asStateFlow()

    private fun onEvent(event: IOSGattEvent) {
        services?.onEvent(event)
    }

    fun scan(): Flow<List<KMMDevice>> {
        return callbackFlow {
            bleState.first { it == CBCentralManagerStatePoweredOn }
            manager.scanForPeripheralsWithServices(null, null)

            scannedDevices.onEach {
                trySend(it)
            }.launchIn(this)

            awaitClose {
                manager.stopScan()
            }
        }
    }

    suspend fun connect(device: KMMDevice) {
        peripheral = (device.device as PeripheralDevice).peripheral
        peripheral.delegate = this
        Napier.i("Connect", tag = TAG)
        bleState.first { it == CBCentralManagerStatePoweredOn }
        return suspendCoroutine { continuation ->
            onDeviceConnected = {
                onDeviceConnected = null
                continuation.resume(Unit)
            }
            Napier.i("Connect peripheral", tag = TAG)
            manager.connectPeripheral(peripheral, null)
        }
    }

    suspend fun disconnect() {
        return suspendCoroutine { continuation ->
            onDeviceDisconnected = {
                onDeviceDisconnected = null
                continuation.resume(Unit)
            }
            manager.cancelPeripheralConnection(peripheral)
        }
    }

    suspend fun discoverServices(): KMMServices {
        Napier.i("Discover services", tag = TAG)
        return suspendCoroutine { continuation ->
            onServicesDiscovered = {
                onServicesDiscovered = null
                val nativeServices = peripheral.services?.map { it as CBService } ?: emptyList()
                val kmmServices = KMMServices(peripheral, nativeServices)
                services = kmmServices
                continuation.resume(kmmServices)
            }
            peripheral.discoverServices(null)
        }
    }

    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
        Napier.i("Did iscover services", tag = TAG)
        peripheral.services?.map {
            it as CBService
        }?.onEach {
            Napier.i("Service uuid: ${it.UUID}", tag = TAG)
            peripheral.discoverCharacteristics(null, it)
        }
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverCharacteristicsForService: CBService,
        error: NSError?,
    ) {
        Napier.i("Discover characteristic", tag = TAG)
        peripheral.services
            ?.map { it as CBService }
            ?.map { it.characteristics }
            ?.mapNotNull { it?.map { it as CBCharacteristic } }
            ?.onEach {
                it.forEach {
                    Napier.i("Characteristic uuid: ${it.UUID}", tag = TAG)
                    peripheral.discoverDescriptorsForCharacteristic(it)
                }
            }
        Napier.i("Discover characteristic: $", tag = TAG)
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didDiscoverDescriptorsForCharacteristic: CBCharacteristic,
        error: NSError?,
    ) {
        Napier.i("Discover descriptors", tag = TAG)
        onServicesDiscovered?.invoke(getOperationStatus(error))
        //todo aggregate responses
    }

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        Napier.i("New state: ${central.state}", tag = TAG)
        _bleState.value = central.state
    }

    override fun centralManager(
        central: CBCentralManager,
        didFailToConnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        Napier.i("didFailToConnectPeripheral", tag = TAG)
        onDeviceConnected?.invoke(DeviceDisconnected)
    }

    override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
        Napier.i("didConnectPeripheral", tag = TAG)
        onDeviceConnected?.invoke(DeviceConnected)
    }

    override fun centralManager(
        central: CBCentralManager,
        didDisconnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        Napier.i("didDisconnectPeripheral", tag = TAG)
        onDeviceDisconnected?.invoke()
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didUpdateValueForCharacteristic: CBCharacteristic,
        error: NSError?,
    ) {
        onEvent(
            OnGattCharacteristicRead(
                peripheral,
                didUpdateValueForCharacteristic.value,
                error
            )
        )
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didWriteValueForCharacteristic: CBCharacteristic,
        error: NSError?,
    ) {
        onEvent(
            OnGattCharacteristicWrite(
                peripheral,
                didWriteValueForCharacteristic.value,
                error
            )
        )
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didWriteValueForDescriptor: CBDescriptor,
        error: NSError?,
    ) {
        onEvent(
            OnGattDescriptorWrite(
                peripheral,
                didWriteValueForDescriptor.value as NSData?,
                error
            )
        )
    }

    override fun peripheral(
        peripheral: CBPeripheral,
        didUpdateValueForDescriptor: CBDescriptor,
        error: NSError?,
    ) {
        onEvent(
            OnGattDescriptorRead(
                peripheral,
                didUpdateValueForDescriptor.value as NSData?,
                error
            )
        )
    }

    override fun centralManager(
        central: CBCentralManager,
        didDiscoverPeripheral: CBPeripheral,
        advertisementData: Map<Any?, *>,
        RSSI: NSNumber,
    ) {
        val kmmDevice = KMMDevice(PeripheralDevice(didDiscoverPeripheral))
        Napier.d { "${advertisementData[CBAdvertisementDataServiceUUIDsKey]}" }
        val uuid = try {
            (advertisementData[CBAdvertisementDataServiceUUIDsKey] as? List<CBUUID>)?.first()?.toUuid()
        } catch (e: Exception) {
            null
        }
        Napier.d { "Uuid: $uuid" }
        if (uuid != BLINKY_SERVICE_UUID) {
            return
        }
        _scannedDevices.value = _scannedDevices.value + kmmDevice
    }

    private fun getOperationStatus(error: NSError?): OperationStatus {
        return error?.let { OperationError } ?: OperationSuccess
    }
}
