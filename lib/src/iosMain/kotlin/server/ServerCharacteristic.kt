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

package server

import client.toByteArray
import client.toNSData
import client.toUuid
import com.benasher44.uuid.Uuid
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import platform.CoreBluetooth.CBATTErrorSuccess
import platform.CoreBluetooth.CBATTRequest
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBMutableCharacteristic
import platform.CoreBluetooth.CBPeripheralManager

private const val TAG = "BLE-TAG"

actual class ServerCharacteristic(
    private val native: CBMutableCharacteristic,
    private val manager: CBPeripheralManager,
    private val notificationsRecords: NotificationsRecords,
) {
    actual val uuid: Uuid = native.UUID.toUuid()

    actual val properties: List<GattProperty> = native.properties.toProperties()

    actual val permissions: List<GattPermission> = emptyList()

    actual val descriptors: List<ServerDescriptor> = native.descriptors
        ?.map { it as CBDescriptor }
        ?.map { ServerDescriptor(it) }
        ?: emptyList()

    private val _value = MutableStateFlow(byteArrayOf())
    actual val value: Flow<ByteArray> = _value

    fun onEvent(event: ServerRequest) {
        when (event) {
            is ReadRequest -> handleReadRequest(event.request)
            is WriteRequest -> handleWriteRequest(event.request)
        }
    }

    private fun handleReadRequest(request: CBATTRequest) {
        if (request.characteristic().UUID != native.UUID) {
            return
        }
        Napier.i("handleReadRequest ${native.UUID}", tag = TAG)
        val dataToSend = _value.value.copyOfRange(
            _value.value.size - request.offset.toInt(),
            _value.value.size
        )

        request.value = dataToSend.toNSData()
        manager.respondToRequest(request, CBATTErrorSuccess)
    }

    private fun handleWriteRequest(requests: List<CBATTRequest>) {
        Napier.i("handleWriteRequest 1: ${native.UUID}", tag = TAG)

        requests.onEach {
            Napier.i("handleWriteRequest 2: ${it.characteristic().UUID}", tag = TAG)
        }
        requests.filter { it.characteristic().UUID == native.UUID }.forEach {
            Napier.i("handleWriteRequest 3: ${it.characteristic().UUID}", tag = TAG)
            it.value?.toByteArray()?.let {
                sendUpdatedValue(it)
            }
            manager.respondToRequest(it, CBATTErrorSuccess)
        }
    }

    actual suspend fun setValue(value: ByteArray) {
        sendUpdatedValue(value)
    }

    private fun sendUpdatedValue(value: ByteArray) {
        Napier.i("set value", tag = TAG)
        _value.value = value
        val centralsToUpdate = notificationsRecords.getCentrals(native.UUID.toUuid())

        val newValue = value.toNSData()

        manager.updateValue(newValue, native, centralsToUpdate)
    }
}
