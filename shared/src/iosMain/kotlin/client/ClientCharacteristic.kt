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

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ClientCharacteristic(
    private val peripheral: CBPeripheral,
    private val native: CBCharacteristic,
) {

    val uuid: Uuid = native.UUID.toUuid()

    private var onCharacteristicWrite: ((OnGattCharacteristicWrite) -> Unit)? = null
    private var onCharacteristicRead: ((OnGattCharacteristicRead) -> Unit)? = null

    private val descriptors = native.descriptors
        ?.map { it as CBDescriptor }
        ?.map { ClientDescriptor(peripheral, it) }
        ?: emptyList()

    internal fun onEvent(event: CharacteristicEvent) {
        when (event) {
            is OnGattCharacteristicRead -> onCharacteristicRead?.invoke(event)
            is OnGattCharacteristicWrite -> onCharacteristicWrite?.invoke(event)
            is DescriptorEvent -> descriptors.onEach { it.onEvent(event) }
        }
    }

    actual fun findDescriptor(uuid: Uuid): ClientDescriptor? {
        return descriptors.firstOrNull { it.uuid == uuid }
    }

    actual suspend fun getNotifications(): Flow<ByteArray> {
        return callbackFlow {
            peripheral.setNotifyValue(true, native)

            onCharacteristicRead = {
                trySend(it.data?.toByteArray() ?: byteArrayOf())
            }

            awaitClose {
                peripheral.setNotifyValue(false, native)
            }
        }
    }

    actual suspend fun write(value: ByteArray, writeType: WriteType) {
        val iosWriteType = when (writeType) {
            WriteType.DEFAULT -> CBCharacteristicWriteWithResponse
            WriteType.NO_RESPONSE -> CBCharacteristicWriteWithoutResponse
        }
        return suspendCoroutine { continuation ->
            onCharacteristicWrite = {
                onCharacteristicWrite = null
                continuation.resume(Unit)
            }
            peripheral.writeValue(value.toNSData(), native, iosWriteType)
        }
    }

    actual suspend fun read(): ByteArray {
        return suspendCoroutine { continuation ->
            onCharacteristicRead = {
                onCharacteristicRead = null
                continuation.resume(it.data?.toByteArray() ?: byteArrayOf())
            }
            peripheral.readValueForCharacteristic(native)
        }
    }
}
